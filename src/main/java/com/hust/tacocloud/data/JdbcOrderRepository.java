package com.hust.tacocloud.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.tacocloud.Order;
import com.hust.tacocloud.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository{

    private SimpleJdbcInsert orderInserter;

    private SimpleJdbcInsert orderTacoInserter;

    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        //lưu thời gian đặt đơn
        order.setPlacedAt(new Date());
        //lưu info order và trả về id
        long orderId = saveOrderDetails(order);
        //setId cho order
        order.setId(orderId);
        // dùng vòng lặp for each để lưu taco vào order ( lưu vào bảng chung)
        for (Taco taco:order.getTacos()) {
            saveTacoToOrder(taco,orderId);
        }

        return order;
    }

    /**
     * save order vào Bảng order
     * @param order
     * @return id
     */
    private long saveOrderDetails(Order order){
        @SuppressWarnings("unchecked")
        //chuyển đối tượng order sang dạng key value để sử dụng SimpleJdbcInsert của JdbcTemplate
        Map<String,Object> values = objectMapper.convertValue(order,Map.class);
        values.put("placedAt",order.getPlacedAt());

        long id = orderInserter.executeAndReturnKey(values).longValue();

        return id;
    }

    /**
     * lưu vào bảng chung( từng taco vào 1 bằng for each ở hàm save sẽ dùng sau)
     * @param taco
     * @param orderId
     */
    private void saveTacoToOrder(Taco taco, long orderId){
        // thêm các trường vào map
        Map<String,Object> values = new HashMap<>();
        values.put("taco",taco.getId());
        values.put("tacoOrder",orderId);

        orderTacoInserter.execute(values);
    }
}
