package com.hust.tacocloud.data;

import com.hust.tacocloud.Ingredient;
import com.hust.tacocloud.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository{

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Từ đối tượng Taco sẽ lưu vào tất cả các 2 bảng Taco_Ingredient và Taco từ 2 hàm ở dưới
     * @param taco
     * @return Taco
     */
    @Override
    public Taco save(Taco taco) {
        // lưu vào bảng Taco
        long id = saveTacoInfo(taco);
        // set id cho Taco để trả về đối tượng taco
        taco.setId(id);
        // lưu vào bảng Taco_Ingredient sử dụng vòng lặp for each
        for (String ingredientId:taco.getIngredients()) {
            saveIngredientToTaco(ingredientId,id);
        }
        return taco;
    }

    /**
     * Lưu taco vào bảng Taco và trả vè id của nó
     * @param taco
     * @return id
     */
    private long saveTacoInfo(Taco taco){
        // Lưu thời gian tạo
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Taco (name, createdAt) " +
                "values (?, ?)", Types.VARCHAR,Types.TIMESTAMP);

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        new Timestamp(taco.getCreatedAt().getTime())));
        // Key Holder có tác dụng để lấy id tự động tăng của taco mà mình vừa tạo
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // lưu taco và lưu id của Taco đấy vào keyHolder
        jdbc.update(psc,keyHolder);
        // trả về id Taco
        return keyHolder.getKey().longValue();
    }

    /**
     * Lưu từng ingredient của taco(cụ thể là lưu từng id của Taco và id của ingredient) vào Bảng chung Ingredient_Taco
     * @param
     * @param
     *
     */
    private void saveIngredientToTaco(String ingredientId,long tacoId){
        jdbc.update("insert into Taco_Ingredients (taco, ingredient)" +
                "values (?, ?)",tacoId,ingredientId);
    }
}
