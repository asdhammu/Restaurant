package my.restaurant.service.impl;

import my.restaurant.dto.CarouselDTO;
import my.restaurant.entity.Carousel;
import my.restaurant.repository.CarouselRepository;
import my.restaurant.service.CarouselService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    private final CarouselRepository carouselRepository;

    public CarouselServiceImpl(CarouselRepository carouselRepository) {
        this.carouselRepository = carouselRepository;
    }

    @Override
    public List<CarouselDTO> getCarousel() {
        List<Carousel> carousels = this.carouselRepository.findAll(Sort.by(Sort.Direction.ASC, "priority"));
        List<CarouselDTO> list = new ArrayList<>();
        boolean isActive = true;
        for (Carousel carousel : carousels) {
            list.add(new CarouselDTO(carousel.getWidth(), carousel.getHeight(),
                    carousel.getTitle(), carousel.getDescription(), carousel.getImgUrl(), isActive));
            isActive = false;
        }
        return list;
    }
}
