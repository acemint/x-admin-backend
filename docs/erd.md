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
    XA_CLINIC ||--|| XA_CLINIC_CREDENTIAL: has
    XA_CLINIC ||--o{ XA_MEMBER : have
    XA_MEMBER ||--o{ XA_ATTENDANCE : creates
    XA_MEMBER ||--o| XA_MEMBER_AS_MANAGER: mightBe
    XA_MEMBER ||--o| XA_MEMBER_AS_PRACTITIONER: mightBe
    XA_MEMBER ||--o| XA_MEMBER_AS_PATIENT: mightBe
    XA_CLINIC ||--o{ XA_ITEM: owns
    
    XA_MEMBER_AS_PRACTITIONER ||--o{ XA_VISIT : confirms
    XA_MEMBER_AS_PATIENT ||--o{ XA_VISIT : confirms
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
    }
    XA_CLINIC_CREDENTIAL {
        string clinicCode PK, FK
        string satuSehatOrganizationKey
        string satuSehatClientKey
        string satuSehatSecretKey
        string satuSehatAccessToken
    }
    XA_MEMBER {
        uuid id PK
        string code "unique"
        uuid clinicId FK
        string username
        string firstName
        string lastName
        date age
        string type
        string role
        string status
        string gender
        string phoneNumber
        string address
        string provinceCode
        string cityCode
        string districtCode
        string villageCode
        string rtCode
        string rwCode
    }
    XA_MEMBER_AS_MANAGER {
        string password
    }
    XA_MEMBER_AS_PRACTITIONER {
        string satuSehatReferenceId "IHS ID from SatuSehat, e.g.: P02478375538"
        decimal salary
        double taxPercentage
    }
    XA_MEMBER_AS_PATIENT {
        string satuSehatReferenceId "IHS ID from SatuSehat, e.g.: P02478375538"
    }
    XA_ATTENDANCE {
        uuid id PK
        uuid memberId FK
        date clockIn
        date clockOut
    }
    XA_VISIT {
        uuid id PK
        uuid patientId FK
        uuid memberId FK
        string satuSehatReferenceId "ID of Encounter from SatuSehat"
        string status
        datetime startTime
        datetime endTime
        datetime sentToSatuSehatTime
    }
    XA_TREATMENT {
        uuid id PK
        string code "unique"
        string referenceId "ID of Encounter from SatuSehat"
        uuid clinidId FK
        string name 
        double price
    }
    XA_CAUSE {
        uuid id PK
        string code "unique"
        string satuSehatReferenceId "ID from SatuSehat"
        uuid clinidId FK
        string name 
    }
    XA_ITEM {
        uuid id PK 
        string code "unique"
        uuid clinicId FK
        string satuSehatReferenceId "ID from SatuSehat"
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
