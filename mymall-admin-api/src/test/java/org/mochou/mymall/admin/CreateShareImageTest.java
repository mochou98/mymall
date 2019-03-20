package org.mochou.mymall.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mochou.mymall.core.qcode.QCodeService;
import org.mochou.mymall.db.domain.MymallGoods;
import org.mochou.mymall.db.service.MymallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateShareImageTest {
    @Autowired
    QCodeService qCodeService;
    @Autowired
    MymallGoodsService mymallGoodsService;

    @Test
    public void test() {
        MymallGoods good = mymallGoodsService.findById(1181010);
        qCodeService.createGoodShareImage(good.getId().toString(), good.getPicUrl(), good.getName());
    }
}
