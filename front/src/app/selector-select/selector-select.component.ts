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
  protected groupedSectors: Map<number, SectorDto[]> = new Map();
  private roots: number[] = [];
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
      } else {
        this.roots.push(sector.sectorId)
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

  getSectorDepthFromRoot(rootId: number, sectorId: number): number | null {
    const queue: { id: number; depth: number }[] = [{ id: rootId, depth: 0 }];

    while (queue.length > 0) {
      const { id, depth } = queue.shift()!;

      if (id === sectorId) {
        return depth;
      }

      const children: SectorDto[] | undefined = this.groupedSectors.get(id);
      if (children) {
        for (const child of children) {
          queue.push({ id: child.sectorId, depth: depth + 1 });
        }
      }
    }

    return null;
  }

  getSectorDepth(sectorId: number): number {
    let depth: number | null = null; // Initialize depth as null

    this.roots.forEach((root: number) => {
      const rootDepth = this.getSectorDepthFromRoot(root, sectorId);
      if (rootDepth !== null) {
        depth = rootDepth; // Update depth if sector is found in any of the roots
      }
    });

    return depth !== null ? depth * 3 : 0; // Return depth or -1 if sector is not found
  }

  protected readonly parseInt = parseInt;
  protected readonly String = String;
}
