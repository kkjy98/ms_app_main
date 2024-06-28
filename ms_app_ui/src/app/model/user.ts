export class User {
    accNo!: string;
    email!: string;
    password!: string;
    firstName!: string;
    lastName!: string;
    username!: string;
    phoneNumber!: string;
    industry!: string;
    reason!: string;
    newsletter!: boolean;
    region!: string;
    token!: string;
    storage!: string;
    ipAddress!: string;
    dataStorage!: string;
    origin!: string;

    toString(): string{
		  return "{AccNo:"+this.accNo+", Email:"+this.email+", Password:"+this.password+", Firstname: "+this.firstName+", Lastname:"+this.lastName
		  +", Username:"+this.firstName+" "+this.lastName+", PhoneNum:"+this.phoneNumber
		  +", Industry:"+this.industry+", Reason:"+this.reason+", Newsletter:"+this.newsletter+", Region:"+this.region+", IP address:"+this.ipAddress+", Storage:"+this.storage+", Data Storage:"+this.dataStorage+"}";
	  }
}
