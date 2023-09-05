import { Component } from '@angular/core';
import {ConnectionService} from "../services/connection.service";
import {SectorDto} from "../dtos/sector-dto";
import {map} from "rxjs";

@Component({
  selector: 'app-selector-select',
  templateUrl: './selector-select.component.html',
  styleUrls: ['./selector-select.component.css']
})
export class SelectorSelectComponent {
  selectedSectors: number[] = []; // Store the selected sector values
  sectors: SectorDto[] = [];
  private connectionService: ConnectionService

  constructor(connectionService: ConnectionService) {
    this.connectionService = connectionService;
  }

  ngOnInit() {
    this.connectionService.get("sector/getAll").pipe(
      map(
        (data: any) => {
          this.sectors = data;
        })).subscribe();
  }

}
