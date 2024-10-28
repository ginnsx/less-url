package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.PlatformDim;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.repository.mapper.PlatformDimMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PlatformDimRepository extends BaseRepository<PlatformDimMapper, PlatformDim> {

    public Long getOrCreatePlatformId(String os, String browser) {
        PlatformDim platformDim = this.lambdaQuery()
                .eq(PlatformDim::getOs, os)
                .eq(PlatformDim::getBrowser, browser)
                .one();

        if (platformDim == null) {
            platformDim = new PlatformDim();
            platformDim.setOs(os);
            platformDim.setBrowser(browser);
            this.save(platformDim);
        }

        return platformDim.getId();
    }
}