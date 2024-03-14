package com.test.helmes.dbos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * This class represents the sector in the database.
 */
@Builder
@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sector", schema = "helmes")
public class SectorDbo {

    @Id
    @Column(name = "sector_id")
    private Integer sectorId;

    @Column(name = "sector_name")
    private String sectorName;

    @Column(name = "sector_parent_id")
    private Integer sectorParentId;
}
