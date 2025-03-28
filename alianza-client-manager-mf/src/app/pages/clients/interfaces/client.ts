export interface Client {
  id?: string;
  sharedKey?: string;
  name: string;
  email: string;
  phone: string;
  startDate: string | null;
  endDate: string | null;
  dataAdded?:string
}
