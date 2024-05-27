package com.satusehat.dto.request.commons;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Address {

    private String use;
    private List<String> line;
    private String city;
    private String postalCode;
    private List<AddressExtension> extension;


    @Data
    @Builder
    public static class AddressExtension {

      private String url;
      private List<DetailAddressExtension> extension;


      @Data
      @Builder
      public static class DetailAddressExtension {

        private String url;
        private String valueCode;

      }
    }

}

//  EXAMPLE:
//  "address": [
//    {
//       "use": "home",
//       "line": [
//          "Gd. Prof. Dr. Sujudi Lt.5, Jl. H.R. Rasuna Said Blok X5 Kav. 4-9 Kuningan"
//       ],
//       "city": "Jakarta",
//       "postalCode": "12950",
//       "country": "ID",
//       "extension": [
//          {
//             "url": "https://fhir.kemkes.go.id/r4/StructureDefinition/administrativeCode",
//             "extension": [
//                {
//                   "url": "province",
//                   "valueCode": "10"
//                },
//                {
//                   "url": "city",
//                   "valueCode": "1010"
//                },
//                {
//                   "url": "district",
//                   "valueCode": "1010101"
//                },
//                {
//                   "url": "village",
//                   "valueCode": "1010101101"
//                },
//                {
//                   "url": "rt",
//                   "valueCode": "2"
//                },
//                {
//                   "url": "rw",
//                   "valueCode": "2"
//                }
//             ]
//          }
//       ]
//    }
// ]