package com.github.GuilhermeBauer16.EstaparTesteTecnico.utils;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;

public class PricingCalculator {

    public static double calculateDynamicMultiplier(GarageModel garage) {

        if (garage.getMaxCapacity() == 0) {
            return 1.0;
        }

        double currentOccupancy = garage.getCurrentOccupancy();
        double maxCapacity = garage.getMaxCapacity();

        double occupancyPercentage = (currentOccupancy / maxCapacity) * 100;

        if (occupancyPercentage < 25.0) {
            return 0.90;
        } else if (occupancyPercentage <= 50.0) {
            return 1.00;
        } else if (occupancyPercentage <= 75.0) {
            return 1.10;
        } else {
            return 1.25;
        }

    }
}
