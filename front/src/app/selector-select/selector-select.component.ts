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
  protected groupedSectors: Map<number, SectorDto[]> = new Map(); // Grouped sectors by parentId
  @Output() selectedSectorEvent: EventEmitter<SectorDto> = new EventEmitter();

  constructor(connectionService: ConnectionService) {
    this.connectionService = connectionService;
  }

  getSelectedSector() {
    this.selectedSectorEvent.emit(this.selectedSector)
  }

  private groupSectorsByParent() {
    this.sectors.forEach((sector: SectorDto) => {
      const parentKey = sector.sectorParentId;
      if (parentKey !== null) {
         if (!this.groupedSectors.has(parentKey)) {
          this.groupedSectors.set(parentKey, []);
        }
        this.groupedSectors.get(parentKey)!.push(sector);
      }
    });
  }
  ngOnInit() {
    this.connectionService.get("sector/getAll").pipe(
      map(
        (data: any) => {
          this.sectors = data;
          this.sectors = this.sectors.sort(x => x.sectorId).reverse();
          this.groupSectorsByParent();
        })).subscribe();
  }

  getSectorName(parentId: number): string {
    const parentSector = this.sectors.find(sector => sector.sectorId === parentId);
    return parentSector ? parentSector.sectorName : '';
  }

}
