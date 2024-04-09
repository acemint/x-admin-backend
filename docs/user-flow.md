## Overview
These diagrams will show the flow of interactions between users / system / SatuSehat, etc.

### 1. Clinic Creation Flow
This flow describes how a clinic can subscribe to our system.  
<br><br><br>
```mermaid
sequenceDiagram
    actor C as Clinic
    actor V as Vendor
    participant BES as Back-End System 
    
    C ->> V: I want to register my clinic.
    V ->> BES: Hit API POST /clinic/register
    V ->> BES: Hit API POST /employee/register (clinic admin)
    BES ->> C: Send Reset Password email
    C ->> C: Reset password
```

### 2. Employee Creation Flow
While there will be an employee is automatically created during "1. Clinic Creation Flow",
A clinic might want to add another employee or add a doctor. This flow will show how to perform it.
<br><br><br>
```mermaid
sequenceDiagram
    actor C1 as Clinic Admin 1
    participant UIS as UI System 
    participant BES as Back-End System 
    participant SS as SatuSehat
    
    C1 ->> UIS: Login using "Username"
    UIS ->> BES: Hit API /public/login
    BES ->> UIS: Set Session ID
    UIS ->> C1: Redirect to Dashboard
    C1 ->> C1: Go to "Employee Registration"
    alt chooses empoloyee type "DOCTOR"
        UIS ->> UIS: Show input field to Search By NIK
        C1 ->> UIS: User inputs NIK
        UIS ->> BES: Send the user inputs & click submit
        BES ->> SS: Hit API search practitioner by NIK
        BES ->> UIS: Return founded user
        C1 ->> UIS: Click Submit
    else chooses employee type "CLINIC_ADMIN"
        UIS ->> UIS: Show all necessary input fields
        C1 ->> UIS: User input all fields & click submit
        UIS ->> BES: Save all data to Backend
        
        create actor C2 as Clinic Admin 2
            BES ->> C2: Data send successfully and send Reset Password email
        destroy C2
    end
```