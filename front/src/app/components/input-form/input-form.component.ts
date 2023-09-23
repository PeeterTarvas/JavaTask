import {Component, OnInit} from '@angular/core';
import {SectorDto} from "../../dtos/sector-dto";
import {CompanyDto} from "../../dtos/company-dto";
import {CompanyWebRequestServiceService} from "../../services/company-web-request-service.service";

@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent implements OnInit {
    name: string = "";
    sector: SectorDto | undefined = undefined;
    terms: boolean = false;
    message: string | undefined;
    sectorId: number | undefined;

  constructor(private connection: CompanyWebRequestServiceService) {
  }
  getName(name: string) {
      this.name = name;
    }
  getSector(sector: SectorDto) {
    this.sector = sector;
  }
  getTerms(terms: boolean) {
    this.terms = terms;
  }
  createCompanyDto(): CompanyDto {
    return {
      companyName: this.name,
      companySectorId: this.sector!.sectorId,
      companyTerms: this.terms
    };
  }

  async getUserCompany(): Promise<CompanyDto> {
    return new Promise<CompanyDto>((resolve, reject) => {
      this.connection.get(sessionStorage.getItem('username') + "/get").then(
        r => r.subscribe(
          (comp) => {
            resolve(comp);
          },
          (error) => {
            reject(error);
          }
        )
      );
    });
  }

  submit() {
    let company: CompanyDto = this.createCompanyDto();
    let username: String = sessionStorage.getItem('username')!;
    this.connection.post(username +'/save', company).then( (response) =>
      {
        if (response && response.message) {
          this.message = response.message;
        }
      })
      .catch((error: any) => {
          this.message = 'Bad request. Please check your input data.';
      });
    }

  async ngOnInit(): Promise<void> {
    const company = await this.getUserCompany();
    this.name = company.companyName;
    this.terms = company.companyTerms;
    this.sectorId = company.companySectorId;
  }



}
