import {Component, EventEmitter, Output} from '@angular/core';
import {SectorDto} from "../dtos/sector-dto";
import {CompanyDto} from "../dtos/company-dto";
import {ConnectionService} from "../services/connection.service";

@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent {
    name: string = "";
    sector: SectorDto | undefined = undefined;
    terms: boolean = false;

  private connectionService: ConnectionService
  constructor(connectionService: ConnectionService) {
    this.connectionService = connectionService;
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
    this.connectionService.post('company/save', company)
  }


}
