import {Component, EventEmitter, Output} from '@angular/core';
import {ConnectionService} from "../services/connection.service";
import {SectorDto} from "../dtos/sector-dto";
import {map} from "rxjs";

@Component({
  selector: 'app-selector-select',
  templateUrl: './selector-select.component.html',
  styleUrls: ['./selector-select.component.css']
})
export class SelectorSelectComponent {
  sectors : SectorDto[] = [];
  private connectionService: ConnectionService
  protected selectedSector: SectorDto | undefined;
  @Output() selectedSectorEvent: EventEmitter<SectorDto> = new EventEmitter();

  constructor(connectionService: ConnectionService) {
    this.connectionService = connectionService;
  }

  getSelectedSector() {
    this.selectedSectorEvent.emit(this.selectedSector)
  }
  ngOnInit() {
    this.connectionService.get("sector/getAll").pipe(
      map(
        (data: any) => {
          this.sectors = data;
        })).subscribe();
  }

}
