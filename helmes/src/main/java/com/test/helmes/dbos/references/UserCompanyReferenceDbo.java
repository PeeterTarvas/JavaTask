package com.test.helmes.dbos.references;


import com.test.helmes.dbos.company.CompanyDbo;
import com.test.helmes.dbos.user.UserDbo;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class is the reference object that ties the user and their company.
 */
@Builder
@Setter
@Getter
@Entity
@ToString
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
    private UserDbo userReference;

    @OneToOne
    @JoinColumn(name = "company_id")
    private CompanyDbo companyReference;
}
