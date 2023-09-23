import {Component, OnInit, ViewChild} from '@angular/core';
import {SectorDto} from "../../dtos/sector-dto";
import {CompanyDto} from "../../dtos/company-dto";
import {CompanyWebRequestServiceService} from "../../services/company-web-request-service.service";
import {SelectorSelectComponent} from "../selector-select/selector-select.component";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";

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
    @ViewChild(SelectorSelectComponent, { static: false }) selectorSelectComponent: SelectorSelectComponent | undefined;


  constructor(private connection: CompanyWebRequestServiceService,
              private authService: AuthenticationService,
              private router: Router) {
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

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
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
    this.sectorId = company.companySectorId
    if (this.selectorSelectComponent) {
      this.sector = this.selectorSelectComponent.setSelectedSector(this.sectorId);
    }
  }



}
