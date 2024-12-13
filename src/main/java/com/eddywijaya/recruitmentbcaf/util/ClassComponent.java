package com.eddywijaya.recruitmentbcaf.util;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ClassComponent {



    @Bean
    public Random getRandom(){
        return new Random();
    }
}
