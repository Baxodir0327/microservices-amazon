//package com.amazon.authservice.service;
//
//import com.example.appreactivesimple.entity.Ketmon;
//import com.example.appreactivesimple.mapper.KetmonMapper;
//import com.example.appreactivesimple.payload.KetmonDTO;
//import com.example.appreactivesimple.repository.KetmonRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class KetmonService {
//
//    private final KetmonRepository ketmonRepository;
//    private final KetmonMapper ketmonMapper;
//
//
//    public Flux<KetmonDTO> list(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ketmonRepository.findAllBy(pageable)
//                .flatMap(this::mapKetmonDTO);
//    }
//
//    private Mono<KetmonDTO> mapKetmonDTO(Ketmon ketmon) {
//        return Mono.just(ketmonMapper.toKetmonDTO(ketmon));
//    }
//
//    public Mono<KetmonDTO> create(KetmonDTO ketmonDTO) {
//        return ketmonRepository.save(ketmonMapper.toKetmon(ketmonDTO))
//                .map(ketmonMapper::toKetmonDTO);
//    }
//
//    public Mono<KetmonDTO> one(UUID id) {
//        Mono<Ketmon> byId = ketmonRepository.findById(id);
//        return byId.flatMap(this::mapKetmonDTO);
//    }
//
//    public Mono<KetmonDTO> update(UUID id, KetmonDTO ketmonDTO) {
//        return ketmonRepository.findById(id).flatMap(ketmon -> {
//            ketmon.setName(ketmonDTO.getName());
//            return ketmonRepository.save(ketmon);
//        }).flatMap(this::mapKetmonDTO);
//    }
//
//    public Mono<Void> remove(UUID id) {
//        return ketmonRepository.deleteById(id);
//    }
//}
