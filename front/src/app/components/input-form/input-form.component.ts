import {Component, EventEmitter, Output} from '@angular/core';
import {SectorDto} from "../../dtos/sector-dto";
import {CompanyDto} from "../../dtos/company-dto";
import {ConnectionService} from "../../services/connection.service";
import {CompanyWebRequestServiceService} from "../../services/company-web-request-service.service";

@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent {
    name: string = "";
    sector: SectorDto | undefined = undefined;
    terms: boolean = false;
    message: string | undefined;

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



}
