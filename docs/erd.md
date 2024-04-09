### Overview 
For every entities placed below, there are auditing columns. This will be used to make sure "who" and "when" 
are the corresponding columns created and last changed. These columns are:
- timestamp createdDate
- string createdBy
- timestamp lastUpdatedDate
- string lastUpdatedBy
<br><br><br>
```mermaid
erDiagram
    XA_CLINIC ||--o{ XA_EMPLOYEE : have
    XA_PRACTITIONER ||--o{ XA_VISIT : confirms
    XA_EMPLOYEE ||--o{ XA_ATTENDANCE : creates
    XA_PATIENT ||--o{ XA_VISIT : confirms
    XA_VISIT ||--o| XA_VISIT_TREATMENT : performs
    XA_VISIT_TREATMENT }|--|| XA_TREATMENT : details
    XA_VISIT_TREATMENT }|--|| XA_CAUSE : details
    
    XA_CLINIC { 
        uuid id PK
        string code "unique" 
        decimal commissionFee 
        decimal sittingFee
        decimal medicalItemFee
        date subscriptionValidFrom
        date subscriptionValidTo
        int subscriptionTier
        string satuSehatOrganizationKey
        string satuSehatClientKey
        string satuSehatSecretKey "bcrypt required"
        string satuSehatAccessToken
    }
    XA_EMPLOYEE {
        uuid id PK
        string code "unique"
        string username
        uuid clinicId FK
        string firstName
        string lastName
        date age
        string type
        string role
        string status
        string gender
        string phoneNumber
        string address
        string password
        decimal salary
        double taxPercentage
    }
    XA_PRACTITIONER {
        uuid id PK
        string reference_id "IHS ID from SatuSehat, e.g.: P02478375538"
        json satuSehatData
    }
    XA_PATIENT {
        uuid id PK
        string reference_id "IHS ID from SatuSehat, e.g.: P02478375538"
        json satuSehatData
    }
    XA_VISIT {
        uuid id PK
        uuid patientId FK
        uuid employeeId FK
        string reference_id "ID of Encounter from SatuSehat"
        string status
        datetime startTime
        datetime endTime
    }
    XA_ATTENDANCE {
        uuid id PK
        uuid employeeId FK
        date clockIn
        date clockOut
    }
    XA_TREATMENT {
        uuid id PK
        string code "unique"
        uuid clinidId FK
        string name 
        double price
    }
    XA_CAUSE {
        uuid id PK
        string code "unique"
        uuid clinidId FK
        string name 
    }
    XA_ITEM {
        uuid id PK 
        string code "unique"
        uuid clinicId FK
        string name
        double quantity
        string status
        string unitOfMeasurement
        date expiryDate
    }
    XA_VISIT_TREATMENT {
        uuid id PK
        uuid visitId FK
        uuid causeId FK
        uuid treatmentId FK
        text description
    }
```
