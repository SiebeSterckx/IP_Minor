package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.boat.controller.BoatDto;
import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatRepository;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)


public class BoatServiceTest {

    @Mock
    BoatRepository boatRepository;

    @InjectMocks
    BoatService boatService;

    @Test
    public void givenNoBoat_whenValidBoatAdded_ThenBoatIsAddedAndBoatIsReturned() {
        // given
        Boat banana = BoatBuilder.aBoatBanana().build();
        BoatDto bananaDto = BoatDtoBuilder.aBoatBanana().build();

        //mocking
        when(boatRepository.save(any())).thenReturn(banana);

        // when
        Boat addedBoat = boatService.createBoat(bananaDto);

        // then
        assertThat(banana.getName()).isSameAs(addedBoat.getName());
    }

    @Test
    public void givenBoatId_whenGetBoat_thenBoatIsReturned() {
        // given
        Boat boat = BoatBuilder.aBoat().build();

        //mocking
        when(boatRepository.findById(anyLong())).thenReturn(Optional.of(boat));

        // when
        Boat foundBoat = boatService.getBoat(boat.getId());

        // then
        assertThat(foundBoat).isEqualTo(boat);
    }

    @Test
    public void givenBoatId_whenGetNonExistingBoat_thenExceptionIsThrown() {
        // given
        long nonExistingBoatId = Long.valueOf(999);

        //mocking
        when(boatRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> boatService.getBoat(nonExistingBoatId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void givenBoatIdAndBoatDto_whenUpdateBoat_thenBoatIsUpdatedAndReturned() {
        // given
        Boat boat = BoatBuilder.aBoat().build();
        BoatDto updatedBoatDto = BoatDtoBuilder.aBoat().withName("Updated Name").build();

        //mocking
        when(boatRepository.findById(anyLong())).thenReturn(Optional.of(boat));
        when(boatRepository.save(any(Boat.class))).thenReturn(boat);

        // when
        Boat updatedBoat = boatService.updateBoat(boat.getId(), updatedBoatDto);

        // then
        assertThat(updatedBoat.getName()).isEqualTo(updatedBoatDto.getName());
    }

    @Test
    public void givenBoatId_whenDeleteBoat_thenBoatIsDeleted() {
        // given
        Boat boat = BoatBuilder.aBoat().build();

        //mocking
        when(boatRepository.findById(anyLong())).thenReturn(Optional.of(boat));

        // when
        boatService.deleteBoat(boat.getId());

        // then
        verify(boatRepository, times(1)).delete(boat);
    }

    @Test
    public void givenInsuranceNumber_whenSearchBoatByInsuranceNumber_thenBoatsAreReturned() {
        // given
        String insuranceNumber = "123456789A";
        List<Boat> boats = Arrays.asList(BoatBuilder.aBoat().withInsuranceNumber(insuranceNumber).build(), BoatBuilder.aBoat().withInsuranceNumber(insuranceNumber).build());

        //mocking
        when(boatRepository.findAllByInsuranceNumber(anyString())).thenReturn(boats);

        // when
        Iterable<Boat> foundBoats = boatService.searchBoatByInsuranceNumber(insuranceNumber);

        // then
        assertThat(foundBoats).containsExactlyElementsOf(boats);
    }

    @Test
    public void givenHeightAndWidth_whenSearchBoatByHeightAndWidth_thenBoatsAreReturned() {
        // given
        int height = 6;
        int width = 4;
        List<Boat> boats = Arrays.asList(BoatBuilder.aBoat().withHeight(height).withWidth(width).build(), BoatBuilder.aBoat().withHeight(height).withWidth(width).build());

        //mocking
        when(boatRepository.findAllByHeightAndAndWidth(anyInt(),anyInt())).thenReturn(boats);

        // when
        Iterable<Boat> foundBoats = boatService.searchBoatByHeightAndWidth(height,width);

        // then
        assertThat(foundBoats).containsExactlyElementsOf(boats);
    }



}
