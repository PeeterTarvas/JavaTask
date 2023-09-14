package com.test.helmes.dbos;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_company_reference", schema = "helmes")
public class UserCompanyReferenceDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_id")
    private Long referenceId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDbo userId;

    @OneToOne
    @JoinColumn(name = "company_id")
    private CompanyDbo companyId;
}
