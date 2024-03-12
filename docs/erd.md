```mermaid
erDiagram
    MS_CLINIC ||--o{ MS_EMPLOYEE : have
    MS_CLINIC ||--o{ MS_PATIENT : have
    MS_CLINIC ||--o{ MS_TREATMENT : have
    MS_CLINIC ||--o{ MS_ITEM : have
    MS_EMPLOYEE ||--o{ MS_VISIT : confirms
    MS_EMPLOYEE ||--o{ MS_ATTENDANCE : creates
    MS_PATIENT ||--o{ MS_VISIT : confirms
    MS_VISIT ||--o| MS_VISIT_TREATMENT : performs
    MS_VISIT_TREATMENT }|--|| MS_TREATMENT : details
    
    MS_CLINIC { 
        uuid id PK
        string code "unique" 
        decimal commissionFee 
        decimal sittingFee
        decimal medicalItemFee
        date subscriptionValidFrom
        date subscriptionValidTo
        int subscriptionTier
    }
    MS_EMPLOYEE {
        uuid id PK
        string code "unique"
        uuid clinicId FK
        string name
        string type
        string role
        string status
        decimal salary
        double taxPercentage
    }
    MS_PATIENT {
        uuid id PK
        string code "unique"
        uuid clinicId FK
        string name
        string phoneNumber
        date age
        string gender
        string email
        string address
    }
    MS_VISIT {
        uuid id PK
        uuid patientId FK
        uuid employeeId FK
        bool cancelled
        datetime start
        datetime end 
    }
    MS_ATTENDANCE {
        uuid id PK
        uuid employeeId FK
        date clockIn
        date clockOut
    }
    MS_TREATMENT {
        uuid id PK
        string code "unique"
        uuid clinidId FK
        string name 
        double price
    }
    MS_ITEM {
        uuid id PK 
        string code "unique"
        uuid clinicId FK
        string name
        double quantity
        string status
        string unitOfMeasurement
        date expiryDate
    }
    MS_VISIT_TREATMENT {
        uuid id PK
        uuid visitId FK
        uuid treatmentId FK
        text description
    }
```
