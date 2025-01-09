import { Component,OnInit, Input, EventEmitter, ElementRef, ViewChild } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { ApiServiceService } from '../api-service.service';
import { FormBuilder, FormGroup } from '@angular/forms';


@Component({
  selector: 'app-data-graph',
  templateUrl: './data-graph.component.html',
  styleUrl: './data-graph.component.css'
})
export class DataGraphComponent {
  @ViewChild('pieChartCanvas') pieChartCanvas!: ElementRef<HTMLCanvasElement>;
  pieChartImageBase64!: string;
  monthForm!:FormGroup;
  @Input() formSubmitted!: EventEmitter<void>;
  fb:any;
  et:any;
  hl:any;
  tp:any;
  availableMonths: string[] = [];

  constructor(
    private apiservice: ApiServiceService,
    private fbl: FormBuilder
  ) {}
  // Pie
  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: false,
  };
  public pieChartLabels = [ [ 'F&B' ], [ 'Household' ], 'Entertainment','Transportation' ];
  public pieChartDatasets = [ {
    data: [ 300, 500, 100,100 ]
  } ];
  public pieChartLegend = true;
  public pieChartPlugins = [];


  //Bar

  public barChartLegend = true;
  public barChartPlugins = [];

  public barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { data: [], label: 'F&B' },
      { data: [], label: 'Entertainment' },
      { data: [], label: 'Household' },
      { data: [], label: 'Transportation' },
    ]
  };

  public barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: false,
  };

  ngOnInit(): void {
    this.loadBarGraph();
    this.populateAvailableMonths();
    // this.loadGraph(this.availableMonths[0]); // Load expenses first
    // Subscribe to the formSubmitted event
    this.monthForm = this.fbl.group({
      selectedMonth: ['']
    });

    if (this.formSubmitted) {
      this.formSubmitted.subscribe(() => {
        this.loadGraph(this.availableMonths[0]); // Refresh the graph when the form is submitted
        this.loadBarGraph();
      });
    }
  }
  loadGraph(month: string) {
    this.apiservice.getTotal(localStorage.getItem('username')).subscribe((res: any) => {
      const data = res[month];
      console.log(res[month]);
      if (data) {
        const fbtotal: any = data.fb;
        const ettotal: any = data.et;
        const hltotal: any = data.hl;
        const tptotal: any = data.tp;

        this.fb = fbtotal;
        this.et = ettotal;
        this.hl = hltotal;
        this.tp = tptotal;

        this.pieChartDatasets = [{
          data: [fbtotal, hltotal, ettotal, tptotal]
        }];
      } else {
        console.error(`No data found for month: ${month}`);
      }
    }, (error: any) => {
      console.error('API call error:', error);
    });
  }


  loadBarGraph() {
    console.log("Loading Bar Graph");
    this.apiservice.getTotal(localStorage.getItem('username')).subscribe((res: any) => {
      const data = res;
      if (data) {
        console.log(data);
        const months = Object.keys(data);
        const fbData: number[] = [];
        const etData: number[] = [];
        const hlData: number[] = [];
        const tpData: number[] = [];
  
        months.forEach(month => {
          const monthData = data[month];
          fbData.push(monthData.fb);
          etData.push(monthData.et);
          hlData.push(monthData.hl);
          tpData.push(monthData.tp);
        });
  
        // Update barChartData
        this.barChartData.labels = months;
        this.barChartData.datasets[0].data = fbData;
        this.barChartData.datasets[1].data = etData;
        this.barChartData.datasets[2].data = hlData;
        this.barChartData.datasets[3].data = tpData;
      } else {
        console.error('No data found');
      }
    }, (error: any) => {
      console.error('API call error:', error);
    });
  }
  
    populateAvailableMonths() {
    this.apiservice.getTotal(localStorage.getItem('username')).subscribe((res: any) => {
      this.availableMonths = Object.keys(res);
      this.availableMonths.sort();

      console.log(this.availableMonths);

      const currentMonth = new Date().toISOString().slice(0, 7); // Get current month in 'YYYY-MM' format
      const defaultMonth = this.availableMonths.includes(currentMonth) ? currentMonth : this.availableMonths[0];
      //this.monthForm.controls['selectedMonth'].setValue(defaultMonth);
      this.loadGraph(defaultMonth);
    }, (error: any) => {
      console.error('Error fetching available months:', error);
    });
  }

  onMonthChange() {
    const selectedMonth = this.monthForm.get('selectedMonth')?.value;
    console.log(selectedMonth);
    if (selectedMonth) {
      console.log("Changing");
      this.loadGraph(selectedMonth);
    }
  }

  savePieChart() {
    const canvas = this.pieChartCanvas.nativeElement;
    const pieChartImageBase64 = canvas.toDataURL('image/png');
    
    console.log(pieChartImageBase64);
    // Create a link element and trigger download
    const link = document.createElement('a');
    link.href = pieChartImageBase64;
    link.download = 'pie-chart.png';
    link.click();
  }

}
