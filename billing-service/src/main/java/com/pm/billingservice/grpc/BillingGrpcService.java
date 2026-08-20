package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    // proto dosyasında olan bu fonksiyonda otomatik generate edildi
    // StreamObserver<BillingResponse> responseObserver ile birden fazla return yapabiliyoruz
    // realtime billing data return edebiliriz gibi çoklu amaçlar için kullanıyoruz.
    @Override
    public void createBillingAccount(BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAcount request received {}", billingRequest.toString());

        // Business logic - e.g save to database, perform calculates etc

        // pretend like generating new account
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();
        responseObserver.onNext(response); // başka bir response data döndüreceksek 1 satırda daha yine bunu yazıp yapabiliriz.
        responseObserver.onCompleted(); // we can end the cycle in this response
    }
}
