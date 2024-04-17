package com.walmart.aex.sizeandpack.listener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.aex.sizeandpack.listener.dto.*;
import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteCc;
import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteFineline;
import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteStyle;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.*;
import com.walmart.aex.sizeandpack.listener.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SPConsumerService {

    private final ObjectMapper objectMapper;
    private final SizeandPackServiceCall sizeandPackServiceCall;
    private final PlmServiceCall plmServiceCall;
    public SPConsumerService(ObjectMapper objectMapper, SizeandPackServiceCall sizeandPackServiceCall, PlmServiceCall plmServiceCall) {
        this.objectMapper = objectMapper;
        this.sizeandPackServiceCall = sizeandPackServiceCall;
        this.plmServiceCall = plmServiceCall;
    }

    public void processMessage(String message) {
        log.info("Processing message:: {}", message);
        try {
            CLPMessageDTO messageDTO = objectMapper.readValue(message, CLPMessageDTO.class);
            String eventType = EventType.valueOf(messageDTO.getHeaders().getType()).name();

            handleEvent(messageDTO.getHeaders(), messageDTO.getPayload(), eventType);
        } catch (Exception e) {
            log.error("Payload not processed and discarded!", e);
        }
    }

    private void handleEvent(HeaderDTO headerDto, PayloadDTO payloadDTO, String eventType) throws JsonProcessingException {
        createSizeAndPackPayload(headerDto, payloadDTO, eventType);
    }

    private void createSizeAndPackPayload(HeaderDTO headerDTO, PayloadDTO payloadDTO, String eventType) throws JsonProcessingException {
        if (payloadDTO != null) {
            SizeAndPackPayloadDTO sizeAndPackPayloadDTO = new SizeAndPackPayloadDTO();
            StrongKeyDTO strongKeyDTO = Optional.ofNullable(headerDTO.getChangeScope())
                    .map(ChangeScopeDTO::getStrongKeys)
                    .orElse(new StrongKeyDTO());
            strongKeyDTO.setPlanId(payloadDTO.getPlanId());
            sizeAndPackPayloadDTO.setPlanId(payloadDTO.getPlanId());
            sizeAndPackPayloadDTO.setPlanDesc(payloadDTO.getPlanDesc());
            sizeAndPackPayloadDTO.setLvl0Nbr(payloadDTO.getLvl0Nbr());
            sizeAndPackPayloadDTO.setLvl0Name(strongKeyDTO.getLvl0GenDesc1());
            Map<String, BigInteger> factoryMap = getPlmQuoteFactoryIdWithCCIDMap(payloadDTO.getPlanId());
            sizeAndPackPayloadDTO.setLvl1List(setLvl1SizeAndPack(headerDTO, payloadDTO, factoryMap));
            log.info("SizeAndPack Payload: {}", objectMapper.writeValueAsString(sizeAndPackPayloadDTO));
            postSizeAndPack(eventType, sizeAndPackPayloadDTO, strongKeyDTO);
        }
    }

    public Map<String, BigInteger> getPlmQuoteFactoryIdWithCCIDMap(Long planId) {
        List<PlmAcceptedQuoteFineline> approvedQuoteFromPlm1 = plmServiceCall.getApprovedQuoteFromPlm(planId, HttpMethod.GET);
        Map<String, BigInteger> factoryMap = new HashMap<>();
        List<PlmAcceptedQuoteCc> plmAcceptedQuoteCcList = approvedQuoteFromPlm1.stream().map(PlmAcceptedQuoteFineline::getPlmAcceptedQuoteStyles).flatMap(Collection::stream).map(PlmAcceptedQuoteStyle::getPlmAcceptedQuoteCcs).flatMap(Collection::stream).collect(Collectors.toList());
        plmAcceptedQuoteCcList.forEach(plmAcceptedQuoteCc -> {
            if(!plmAcceptedQuoteCc.getPlmAcceptedQuotes().isEmpty()) {
                factoryMap.put(plmAcceptedQuoteCc.getCustomerChoice(), plmAcceptedQuoteCc.getPlmAcceptedQuotes().iterator().next().getFactoryId());
            }
        });
        return factoryMap;
    }

    private List<SizeAndPackLvl1DTO> setLvl1SizeAndPack(HeaderDTO headerDTO, PayloadDTO payloadDTO, Map<String, BigInteger> factoryMap) {
        SizeAndPackLvl1DTO sizeAndPackLvl1DTO = new SizeAndPackLvl1DTO();
        List<SizeAndPackLvl1DTO> sizeAndPackLvl1DTOList = new ArrayList<>();
        sizeAndPackLvl1DTO.setLvl1Nbr(payloadDTO.getLvl1Nbr());
        sizeAndPackLvl1DTO.setLvl1Name(headerDTO.getChangeScope().getStrongKeys().getLvl1GenDesc1());
        sizeAndPackLvl1DTO.setLvl2List(setLvl2SizeAndPack(headerDTO, payloadDTO, factoryMap));
        sizeAndPackLvl1DTOList.add(sizeAndPackLvl1DTO);
        return sizeAndPackLvl1DTOList;
    }

    private List<SizeAndPackLvl2DTO> setLvl2SizeAndPack(HeaderDTO headerDTO, PayloadDTO payloadDTO, Map<String, BigInteger> factoryMap) {
        SizeAndPackLvl2DTO sizeAndPackLvl2DTO = new SizeAndPackLvl2DTO();
        List<SizeAndPackLvl2DTO> sizeAndPackLvl2DTOList = new ArrayList<>();
        sizeAndPackLvl2DTO.setLvl2Nbr(payloadDTO.getLvl2Nbr());
        sizeAndPackLvl2DTO.setLvl2Name(headerDTO.getChangeScope().getStrongKeys().getLvl2GenDesc1());
        sizeAndPackLvl2DTO.setLvl3List(setLvl3SizeAndPack(headerDTO, Optional.ofNullable(payloadDTO.getLvl3()).orElse(new Lvl3DTO()), factoryMap));
        sizeAndPackLvl2DTOList.add(sizeAndPackLvl2DTO);
        return sizeAndPackLvl2DTOList;
    }

    private List<SizeAndPackLvl3DTO> setLvl3SizeAndPack(HeaderDTO headerDTO, Lvl3DTO lvl3DTO, Map<String, BigInteger> factoryMap) {
        List<SizeAndPackLvl3DTO> sizePackLvl3DTOList = new ArrayList<>();
        SizeAndPackLvl3DTO sizePackLvl3DTO = new SizeAndPackLvl3DTO();
        sizePackLvl3DTO.setLvl3Nbr(lvl3DTO.getLvl3Nbr());
        sizePackLvl3DTO.setLvl3Name(lvl3DTO.getLvl3Name());
        sizePackLvl3DTO.setLvl4List(setLvl4SizeAndPack(headerDTO, Optional.ofNullable(lvl3DTO.getLvl4()).orElse(new Lvl4DTO()), factoryMap));
        sizePackLvl3DTOList.add(sizePackLvl3DTO);
        return sizePackLvl3DTOList;
    }

    private List<SizeAndPackLvl4DTO> setLvl4SizeAndPack(HeaderDTO headerDTO, Lvl4DTO lvl4DTO, Map<String, BigInteger> factoryMap) {
        List<SizeAndPackLvl4DTO> sizePackLvl4DTOList = new ArrayList<>();
        SizeAndPackLvl4DTO sizePackLvl4DTO = new SizeAndPackLvl4DTO();
        sizePackLvl4DTO.setLvl4Nbr(lvl4DTO.getLvl4Nbr());
        sizePackLvl4DTO.setLvl4Name(lvl4DTO.getLvl4Name());
        sizePackLvl4DTO.setFinelines(setFinelineSizePack(headerDTO, Optional.ofNullable(lvl4DTO.getFinelines()).orElse(new FinelinePayloadDTO()), factoryMap));
        sizePackLvl4DTOList.add(sizePackLvl4DTO);
        return sizePackLvl4DTOList;
    }


    private List<SizeAndPackFinelinesDTO> setFinelineSizePack(HeaderDTO headerDTO, FinelinePayloadDTO finelines, Map<String, BigInteger> factoryMap) {
        List<SizeAndPackFinelinesDTO> sizepackFinelineDTOList = new ArrayList<>();
        sizepackFinelineDTOList.add(getSizeAndPackFinelinesDTO(finelines, headerDTO, factoryMap));
        return sizepackFinelineDTOList;
    }

    private SizeAndPackFinelinesDTO getSizeAndPackFinelinesDTO(FinelinePayloadDTO finelines, HeaderDTO headerDTO, Map<String, BigInteger> factoryMap) {
        SizeAndPackFinelinesDTO sizePackFinelinesDTO = new SizeAndPackFinelinesDTO();
        Long finelineNbr = headerDTO.getChangeScope().getStrongKeys().getFineline().getFinelineId();
        sizePackFinelinesDTO.setFinelineNbr(finelineNbr);
        sizePackFinelinesDTO.setFinelineName(finelines.getFinelineName());
        sizePackFinelinesDTO.setAltFinelineName(finelines.getAltFinelineName());
        sizePackFinelinesDTO.setChannel(finelines.getChannel());
        List<SizeAndPackStyleDTO> sizePackStyleDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(finelines.getStyles())) {
            finelines.getStyles().forEach(styleDTO -> sizePackStyleDTOList.add(setStyleSizePack(styleDTO, factoryMap)));
        }
        sizePackFinelinesDTO.setStyles(sizePackStyleDTOList);
        return sizePackFinelinesDTO;
    }

    private SizeAndPackStyleDTO setStyleSizePack(StyleDTO styleDTO, Map<String, BigInteger> factoryMap) {
        SizeAndPackStyleDTO sizePackStyleDTO = new SizeAndPackStyleDTO();
        sizePackStyleDTO.setStyleNbr(styleDTO.getStyleNbr());
        sizePackStyleDTO.setAltStyleDesc(styleDTO.getAltStyleDesc());
        sizePackStyleDTO.setChannel(styleDTO.getChannel());
        List<SizeAndPackCustomerChoiceDTO> sizePackCustomerChoiceDTOList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(styleDTO.getCustomerChoices())) {
            styleDTO.getCustomerChoices().forEach(customerChoiceDTO -> sizePackCustomerChoiceDTOList.add(getCustomerChoiceSizePack(customerChoiceDTO, factoryMap)));
        }

        sizePackStyleDTO.setCustomerChoices(sizePackCustomerChoiceDTOList);
        return sizePackStyleDTO;
    }

    private SizeAndPackCustomerChoiceDTO getCustomerChoiceSizePack(CustomerChoiceDTO customerChoiceDTO, Map<String, BigInteger> factoryMap) {
        SizeAndPackCustomerChoiceDTO sizePackCustomerChoiceDTO = new SizeAndPackCustomerChoiceDTO();
        sizePackCustomerChoiceDTO.setCcId(customerChoiceDTO.getCcId());
        sizePackCustomerChoiceDTO.setAltCcDesc(customerChoiceDTO.getAltCcDesc());
        sizePackCustomerChoiceDTO.setChannel(customerChoiceDTO.getChannel());

        TypeSpecificDTO typeSpecificDTO = Optional.ofNullable(customerChoiceDTO.getMetrics())
                .map(MetricsDTO::getCurrent)
                .map(CurrentMetricsDTO::getStore)
                .map(ChannelMetricsDTO::getProductAttributes)
                .map(ProductAttributesDTO::getTypeSpecific)
                .orElse(new TypeSpecificDTO());

        TypeSpecificDTO typeSpecificDTOForOnline = Optional.ofNullable(customerChoiceDTO.getMetrics())
                .map(MetricsDTO::getCurrent)
                .map(CurrentMetricsDTO::getOnline)
                .map(ChannelMetricsDTO::getProductAttributes)
                .map(ProductAttributesDTO::getTypeSpecific)
                .orElse(new TypeSpecificDTO());

        if (!CollectionUtils.isEmpty(typeSpecificDTO.getColors())) {
            sizePackCustomerChoiceDTO.setColorName(typeSpecificDTO.getColors().get(0).getColorName());
            sizePackCustomerChoiceDTO.setColorFamily(typeSpecificDTO.getColors().get(0).getColorFamily());
        }
        if (!CollectionUtils.isEmpty(typeSpecificDTOForOnline.getColors())) {
            sizePackCustomerChoiceDTO.setColorName(typeSpecificDTOForOnline.getColors().get(0).getColorName());
        }

        setConstraints(typeSpecificDTO, typeSpecificDTOForOnline, sizePackCustomerChoiceDTO, factoryMap);
        return sizePackCustomerChoiceDTO;
    }

    /**
     * Set Constraints for Store/Online
     *
     * @param storeSpecificDTO - Store Type DTO
     * @param onlineSpecificDTO - Online Type DTO
     * @param sizeAndPackCustomerChoiceDTO - CustomerChoice DTO
     */
    private void setConstraints(TypeSpecificDTO storeSpecificDTO, TypeSpecificDTO onlineSpecificDTO, SizeAndPackCustomerChoiceDTO sizeAndPackCustomerChoiceDTO, Map<String, BigInteger> factoryMap) {
        ColorCombinationConstraintsDTO colorCombinationConstraints = new ColorCombinationConstraintsDTO();

        if (!CollectionUtils.isEmpty(storeSpecificDTO.getSuppliers()))
            colorCombinationConstraints.setSuppliers(storeSpecificDTO.getSuppliers());
        else if (!CollectionUtils.isEmpty(onlineSpecificDTO.getSuppliers())) {
            colorCombinationConstraints.setSuppliers(onlineSpecificDTO.getSuppliers());
        }

        if(factoryMap.containsKey(sizeAndPackCustomerChoiceDTO.getCcId())) {
            colorCombinationConstraints.setFactoryId(String.valueOf(factoryMap.get(sizeAndPackCustomerChoiceDTO.getCcId())));
        }
        sizeAndPackCustomerChoiceDTO.setConstraints(new ConstraintsDTO(colorCombinationConstraints, new FinelineLevelConstraintsDTO()));
    }

    private void postSizeAndPack(String eventType, SizeAndPackPayloadDTO sizeAndPackPayloadDTO, StrongKeyDTO strongKeyDTO) throws JsonProcessingException {
        if (eventType.equalsIgnoreCase(EventType.CREATE.name()) || eventType.equalsIgnoreCase(EventType.INITIAL_LOAD.name())) {
            String response = sizeandPackServiceCall.postEventSizePackService(sizeAndPackPayloadDTO, HttpMethod.POST);
            log.info("Post create event : {}", response);
        } else if (eventType.equalsIgnoreCase(EventType.UPDATE.name())) {
            String response = sizeandPackServiceCall.postEventSizePackService(sizeAndPackPayloadDTO, HttpMethod.PUT);
            log.info("Put update event : {}", response);
        } else if (eventType.equalsIgnoreCase(EventType.DELETE.name())) {
            SizeAndPackDeletePayloadDTO sizeAndPackDeletePayloadDTO = new SizeAndPackDeletePayloadDTO();
            sizeAndPackDeletePayloadDTO.setSizeAndPackPayloadDTO(sizeAndPackPayloadDTO);
            sizeAndPackDeletePayloadDTO.setStrongKey(getStrongKey(strongKeyDTO, sizeAndPackPayloadDTO.getPlanDesc()));
            log.info("Size and Pack Delete Payload: {}", objectMapper.writeValueAsString(sizeAndPackDeletePayloadDTO));
            String response = sizeandPackServiceCall.deleteEventSizePackService(sizeAndPackDeletePayloadDTO, HttpMethod.DELETE);
            log.info("Post delete event : {}", response);
        }
    }

    private SizeAndPackStrongKeyDTO getStrongKey(StrongKeyDTO strongKeyDTO, String planDesc) {
        SizeAndPackStrongKeyDTO sizeAndPackStrongKeyDTO = new SizeAndPackStrongKeyDTO();
        sizeAndPackStrongKeyDTO.setPlanId(strongKeyDTO.getPlanId());
        sizeAndPackStrongKeyDTO.setPlanDesc(planDesc);
        sizeAndPackStrongKeyDTO.setLvl0Nbr(strongKeyDTO.getLvl0Nbr());
        sizeAndPackStrongKeyDTO.setLvl1Nbr(strongKeyDTO.getLvl1Nbr());
        sizeAndPackStrongKeyDTO.setLvl2Nbr(strongKeyDTO.getLvl2Nbr());
        sizeAndPackStrongKeyDTO.setLvl3Nbr(strongKeyDTO.getLvl3Nbr());
        sizeAndPackStrongKeyDTO.setLvl4Nbr(strongKeyDTO.getLvl4Nbr());

        StrategyFinelinesDTO strategyFinelinesDTO = new StrategyFinelinesDTO();
        StrongKeyFinelineDTO strongKeyFinelineDTO = Optional.ofNullable(strongKeyDTO.getFineline()).orElse(new StrongKeyFinelineDTO());
        strategyFinelinesDTO.setFinelineNbr(strongKeyFinelineDTO.getFinelineId());
        List<StrategyStyleDTO> strategyStyleDTOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(strongKeyFinelineDTO.getStyles())) {
            strongKeyFinelineDTO.getStyles().forEach(strongKeyStyleDTO -> {
                StrategyStyleDTO strategyStyleDTO = new StrategyStyleDTO();
                strategyStyleDTO.setStyleNbr(strongKeyStyleDTO.getStyleId());
                List<StrategyCustomerChoiceDTO> strategyCustomerChoiceDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(strongKeyStyleDTO.getCcIds())) {
                    strongKeyStyleDTO.getCcIds().forEach(ccId -> {
                        StrategyCustomerChoiceDTO strategyCustomerChoiceDTO = new StrategyCustomerChoiceDTO();
                        strategyCustomerChoiceDTO.setCcId(ccId);
                        strategyCustomerChoiceDTOS.add(strategyCustomerChoiceDTO);
                    });
                }
                strategyStyleDTO.setCustomerChoices(strategyCustomerChoiceDTOS);
                strategyStyleDTOS.add(strategyStyleDTO);
            });
            strategyFinelinesDTO.setStyles(strategyStyleDTOS);
        }
        sizeAndPackStrongKeyDTO.setFineline(strategyFinelinesDTO);
        return sizeAndPackStrongKeyDTO;
    }

}
