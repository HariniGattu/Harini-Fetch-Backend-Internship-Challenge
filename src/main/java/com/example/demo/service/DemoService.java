package com.example.demo.service;

import com.example.demo.model.AddRequest;
import com.example.demo.model.SpendRequest;
import com.example.demo.model.SpendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemoService {
    private final List<AddRequest> payersList = new ArrayList<>();

    public List<AddRequest> add(AddRequest addRequest) {
        Optional<AddRequest> demoModelOptional = payersList.stream().filter(it -> addRequest.getPayer().equalsIgnoreCase(it.getPayer())).findAny();
        demoModelOptional.ifPresentOrElse(
                it -> { it.setPoints(String.valueOf(Integer.parseInt(it.getPoints()) + Integer.parseInt(addRequest.getPoints()))); it.setTimestamp(Instant.now()); },
                () -> { addRequest.setTimestamp(Instant.now()); payersList.add(addRequest); });
        return payersList;
    }


    public List<SpendResponse> spend(SpendRequest addRequestModel) {
        List<SpendResponse> spendResponseList = new ArrayList<>();
        int total_points = payersList.stream().mapToInt(it -> Integer.parseInt(it.getPoints())).sum();
        if(!payersList.isEmpty() && total_points >= Integer.parseInt(addRequestModel.getPoints())) {
            payersList.sort(Comparator.comparing(AddRequest::getTimestamp));
            payersList.forEach(it -> {
                SpendResponse spendResponse = new SpendResponse();
                if(Integer.parseInt(addRequestModel.getPoints()) >= 0 && Integer.parseInt(it.getPoints()) >= 0) {
                    if(Integer.parseInt(addRequestModel.getPoints()) >= Integer.parseInt(it.getPoints())) {
                        spendResponse.setPayer(it.getPayer());
                        spendResponse.setPoints("-" + it.getPoints());
                        addRequestModel.setPoints(String.valueOf(Integer.parseInt(addRequestModel.getPoints()) - Integer.parseInt(it.getPoints())));
                        it.setPoints("0");
                    }
                    else {
                        spendResponse.setPayer(it.getPayer());
                        spendResponse.setPoints(addRequestModel.getPoints().equalsIgnoreCase("0") ? "0" : "-" + addRequestModel.getPoints());
                        it.setPoints(String.valueOf(Integer.parseInt(it.getPoints()) - Integer.parseInt(addRequestModel.getPoints())));
                        addRequestModel.setPoints("0");
                    }
                }
                spendResponseList.add(spendResponse);
            });
        }
        return spendResponseList;
    }

    public List<String> balance() {
        List<String> resultList = new ArrayList<>();
        payersList.forEach(it -> {
            resultList.add(it.getPayer() + " : " + it.getPoints());
        });
        return resultList;
    }
}
