export class Expense {
    amount!: string;
    category!: string;
    date!: string;
    description!: string;
    username!: string;

    toString(): string {
        return "{Amount: " + this.amount + ", Category: " + this.category + ", Date: " + this.date + ", Description: " + this.description + ", Username: " + this.username + "}";
    }
}

