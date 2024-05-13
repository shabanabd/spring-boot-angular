export class Application {
  id: string = '';
  salesAgentFirstName: string = '';
  salesAgentLastName: string = '';
  salesAgentEmail: string = '';
  accountType: string = '';
  createdAt: Date = new Date();
  applicationStatus: string = '';
  businessCategory: string = '';
  updatedAt: Date = new Date();
}
export interface  ApplicationResponse {
  content: Application[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;

}
