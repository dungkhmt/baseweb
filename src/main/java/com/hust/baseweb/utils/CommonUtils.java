package com.hust.baseweb.utils;

import com.google.gson.Gson;
import com.hust.baseweb.model.querydsl.SearchCriteria;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.model.querydsl.SortCriteria;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    public static Logger LOG = LoggerFactory.getLogger(CommonUtils.class);
    public static int SEQ_ID_LEN = 6;

    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2MapObject(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        return map;
    }

    public static Sort buildSortBySortCriteria(SortCriteria[] sort) {
        if (sort.length == 0) {
            return null;
        }
        List<Order> lorder = new ArrayList<>();
        for (int i = 0; i < sort.length; i++) {
            if (sort[i].isAsc()) {
                lorder.add(new Sort.Order(Sort.Direction.ASC, sort[i].getField()));
            } else {
                lorder.add(new Sort.Order(Sort.Direction.DESC, sort[i].getField()));
            }
        }
        return Sort.by(lorder);

    }

    public static Sort buildSortBySortCriteria(List<SortCriteria> sort) {
        if (sort.size() == 0) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        for (SortCriteria sortCriteria : sort) {
            if (sortCriteria.isAsc()) {
                orders.add(new Order(Sort.Direction.ASC, sortCriteria.getField()));
            } else {
                orders.add(new Order(Sort.Direction.DESC, sortCriteria.getField()));
            }
        }
        return Sort.by(orders);

    }

    public static SortAndFiltersInput rebuildQueryDsl(Map<String, String> map, SortAndFiltersInput query) {
        SortCriteria[] sort = query.getSort();
        SearchCriteria[] filter = query.getFilters();
        if (sort != null) {
            for (SortCriteria sortCriteria : sort) {
                sortCriteria.setField(map.get(sortCriteria.getField()));
            }
        }
        if (filter != null) {
            for (SearchCriteria searchCriteria : filter) {
                searchCriteria.setKey(map.get(searchCriteria.getKey()));
            }
        }
        return new SortAndFiltersInput(filter, sort);
    }

    public static SortAndFiltersInput rebuildQueryDsl(
        @SuppressWarnings("rawtypes") Pair<Map<String, String>, Map<String, Pair>> pair,
        SortAndFiltersInput query
    ) {
        SortCriteria[] sort = query.getSort();
        SearchCriteria[] filter = query.getFilters();
        if (sort != null) {
            for (SortCriteria sortCriteria : sort) {
                sortCriteria.setField(buildQueryDslPath(pair, sortCriteria.getField()));
            }
        }

        if (filter != null) {
            for (SearchCriteria searchCriteria : filter) {
                searchCriteria.setKey(buildQueryDslPath(pair, searchCriteria.getKey()));
            }
        }
        return new SortAndFiltersInput(filter, sort);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String buildQueryDslPath(Pair<Map<String, String>, Map<String, Pair>> pair, String field) {
        String[] elm = field.split("\\.");
        StringBuilder result = new StringBuilder();
        Pair<Map<String, String>, Map<String, Pair>> pairTmp = pair;
        for (int i = 0; i < elm.length - 1; i++) {
            result.append(pairTmp.getValue0().get(elm[i]).length() != 0 ? (pairTmp.getValue0().get(elm[i]) + ".") : "");
            pairTmp = pairTmp.getValue1().get(elm[i]);
        }
        result.append(pairTmp.getValue0().get(elm[elm.length - 1]));
        return result.toString();
    }

    public static String buildSeqId(int idx) {
        StringBuilder stringBuilder = new StringBuilder(idx + "");
        while (stringBuilder.length() < SEQ_ID_LEN) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }
}
