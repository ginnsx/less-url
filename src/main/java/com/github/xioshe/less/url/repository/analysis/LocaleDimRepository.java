package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.LocaleDim;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.repository.mapper.LocaleDimMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LocaleDimRepository extends BaseRepository<LocaleDimMapper, LocaleDim> {

    public Long getOrCreateLocaleId(String timezone, String language) {
        LocaleDim localeDim = this.lambdaQuery()
                .eq(LocaleDim::getTimezone, timezone)
                .eq(LocaleDim::getLanguage, language)
                .one();

        if (localeDim == null) {
            localeDim = new LocaleDim();
            localeDim.setTimezone(timezone);
            localeDim.setLanguage(language);
            this.save(localeDim);
        }

        return localeDim.getId();
    }
}