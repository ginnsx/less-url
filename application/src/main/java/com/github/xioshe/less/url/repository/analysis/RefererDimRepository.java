package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.RefererDim;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.mapper.RefererDimMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefererDimRepository extends BaseRepository<RefererDimMapper, RefererDim> {

    public Long getOrCreateRefererId(String type, String referer) {
        RefererDim refererDim = this.lambdaQuery()
                .eq(RefererDim::getRefererType, type)
                .eq(RefererDim::getReferer, referer)
                .one();

        if (refererDim == null) {
            refererDim = new RefererDim();
            refererDim.setRefererType(type);
            refererDim.setReferer(referer);
            this.save(refererDim);
        }

        return refererDim.getId();
    }
}