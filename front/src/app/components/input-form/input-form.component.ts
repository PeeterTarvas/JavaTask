import {Component, OnInit, ViewChild} from '@angular/core';
import {SectorDto} from "../../dtos/sector-dto";
import {CompanyDto} from "../../dtos/company-dto";
import {CompanyWebRequestServiceService} from "../../services/company-web-request-service.service";
import {SelectorSelectComponent} from "../selector-select/selector-select.component";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";

/**
 * This is the form for the user to view, save and update their company.
 * All company related actions are called from this class.
 */
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
  setName(name: string) {
      this.name = name;
    }
  setSector(sector: SectorDto) {
    this.sector = sector;
  }
  setTerms(terms: boolean) {
    this.terms = terms;
  }

  /**
   * This method creates a CompanyDto object to be saved and returns it.
   */
  createCompanyDto(): CompanyDto {
    return {
      companyName: this.name,
      companySectorId: this.sector!.sectorId,
      companyTerms: this.terms
    };
  }

  /**
   * This method gets the users company if the company does not exist then it returns an error.
   * It is used for the initial company loading.
   */
  async getUserCompany(): Promise<CompanyDto> {
    return new Promise<CompanyDto>((resolve, reject) => {
      this.connection.get(sessionStorage.getItem('username') + "/get").then(
        (r) => {
          try {
            r.subscribe(
              (comp) => {
                resolve(comp);
              })
          } catch (e) {

          }
        }
      );
    });
  }

  /**
   * Method that posts a new company.
   * @param username of the user saving a company.
   * @param company object that holds the new company details.
   */
  createNewCompany(username: string, company: CompanyDto) {
    return this.connection.post(username +'/save', company)
  }

  /**
   * Method updates the users company.
   * @param username of the user saving a company.
   * @param company object that holds the details of the updated company.
   */
  updateCompany(username: string, company: CompanyDto) {
    return this.connection.put(username +'/update', company)
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * This method submits the users company details.
   * This method eiter saves or updates the users company with the details the user has provided.
   * If sectorId attribute has been provided then it means that the user already has a company and therefore this
   * method updates the already existing company with a put request.
   * Else it creates a new company for the user.
   */
  submit() {
    let company: CompanyDto = this.createCompanyDto();
    let username: string = sessionStorage.getItem('username')!;
    let resp;
    if (this.sectorId !== undefined) {
      resp = this.updateCompany(username, company);
    } else {
      resp = this.createNewCompany(username, company);
    }
    resp.then( (response) =>
      {
        if (response && response.message) {
          this.message = response.message;
          this.sectorId = company.companySectorId;
        }
      })
      .catch((error: any) => {
          this.message = 'Bad request. Please check your input data.';
      });
  }

  /**
   * This method is initialized when the page is loaded and loads the users company details if the user has a company.
    */
  async ngOnInit(): Promise<void> {
    let company;
    try {
      company = await this.getUserCompany();
      this.name = company.companyName;
      this.terms = company.companyTerms;
      this.sectorId = company.companySectorId
      if (this.selectorSelectComponent) {
        this.sector = this.selectorSelectComponent.setSelectedSector(this.sectorId);
      }
    } catch (e) {

    }
  }



}
