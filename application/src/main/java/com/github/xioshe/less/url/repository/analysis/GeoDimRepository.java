package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.GeoDim;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.mapper.GeoDimMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GeoDimRepository extends BaseRepository<GeoDimMapper, GeoDim> {

    public Long getOrCreateGeoId(String country, String city, String region, String continent) {
        GeoDim geoDim = this.lambdaQuery()
                .eq(GeoDim::getCountry, country)
                .eq(GeoDim::getRegion, region)
                .eq(GeoDim::getCity, city)
                .one();

        if (geoDim == null) {
            geoDim = new GeoDim();
            geoDim.setCountry(country);
            geoDim.setRegion(region);
            geoDim.setCity(city);
            geoDim.setContinent(continent);
            this.save(geoDim);
        }

        return geoDim.getId();
    }
}