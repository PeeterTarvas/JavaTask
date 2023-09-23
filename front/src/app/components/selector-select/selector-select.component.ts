import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ConnectionService} from "../../services/connection.service";
import {SectorDto} from "../../dtos/sector-dto";
import {map} from "rxjs";
import {SectorWebRequestServiceService} from "../../services/sector-web-request-service.service";

@Component({
  selector: 'app-selector-select',
  templateUrl: './selector-select.component.html',
  styleUrls: ['./selector-select.component.css']
})
export class SelectorSelectComponent implements OnInit {
  sectors : SectorDto[] = [];
  selectedSector: SectorDto | undefined;
  protected groupedSectors: Map<number, SectorDto[]> = new Map();
  private roots: number[] = [];
  @Output() selectedSectorEvent: EventEmitter<SectorDto> = new EventEmitter();

  constructor(private connectionService: SectorWebRequestServiceService) {
  }

  getSelectedSector() {
    this.selectedSectorEvent.emit(this.selectedSector)
  }

  setSelectedSector(sectorId: number) {
    this.selectedSector =  this.sectors.find(x => x.sectorId === sectorId)
    return this.selectedSector;
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

  async getAll() {
    this.connectionService.get("getAll").then( (data) => {
      this.sectors = data;
      this.sectors = this.sectors.sort(x => x.sectorId).reverse();
      this.groupSectorsByParent();
    })
  }
  ngOnInit() {
    this.getAll();
  }

  getSectorName(parentId: number): string {
    const parentSector = this.sectors.find(sector => sector.sectorId === parentId);
    return parentSector ? parentSector.sectorName : '';
  }

  /**
   * Breath-first search with while loop
   * Search all the nodes if it is the searched sector
   */
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

  /**
   * To make the sector and the sector structure work visually
   * we need to find what is the parent-child relationship depth for all the sectors
   *
   * This is done by finding all the root sectors - whose parent === null -
   * and then traversing those sector trees with Breath-First Search.
   * BFS is slow but in this case we won't have small trees so no need to worry.
   * Method does BFS for all the trees and finds the element depth from the multiple trees.
   * The tree root nodes here are called roots as well.
   * We use a helper method getSectorDepthFromRoot, which is BFS itself,
   * to find if sector exists in that tree and the depth of that sector.
   */
  getSectorDepth(sectorId: number): number {
    let depth: number | null = null;

    this.roots.forEach((root: number) => {
      const rootDepth = this.getSectorDepthFromRoot(root, sectorId);
      if (rootDepth !== null) {
        depth = rootDepth;
      }
    });

    return depth !== null ? depth * 3 : 0;
  }
}
