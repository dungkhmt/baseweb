package com.hust.baseweb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hust.baseweb.model.querydsl.SearchCriteria;
import com.hust.baseweb.model.querydsl.SortAndFiltersInput;
import com.hust.baseweb.model.querydsl.SortCriteria;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class CommonUtils {
    public static Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2MapObject(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        return map;
    }

    public static Sort buildSortbySortCriteria(SortCriteria sort[]) {
        if (sort.length == 0)
            return null;
        List<Order> lorder = new ArrayList<Sort.Order>();
        for (int i = 0; i < sort.length; i++) {
            if (sort[i].isAsc()) {
                lorder.add(new Sort.Order(Sort.Direction.ASC, sort[i].getField()));
            } else {
                lorder.add(new Sort.Order(Sort.Direction.DESC, sort[i].getField()));
            }
        }
        return Sort.by(lorder);

    }

    public static Sort buildSortbySortCriteria(List<SortCriteria> sort) {
        if (sort.size() == 0)
            return null;
        List<Order> lorder = new ArrayList<Sort.Order>();
        for (int i = 0; i < sort.size(); i++) {
            if (sort.get(i).isAsc()) {
                lorder.add(new Sort.Order(Sort.Direction.ASC, sort.get(i).getField()));
            } else {
                lorder.add(new Sort.Order(Sort.Direction.DESC, sort.get(i).getField()));
            }
        }
        return Sort.by(lorder);

    }

    public static SortAndFiltersInput rebuildQueryDsl(Map<String, String> map, SortAndFiltersInput query) {
        SortCriteria[] sort = query.getSort();
        SearchCriteria[] filter = query.getFilters();
        if (sort != null)
            for (int i = 0; i < sort.length; i++) {
                sort[i].setField(map.get(sort[i].getField()));
            }
        if (filter != null)
            for (int i = 0; i < filter.length; i++) {
                filter[i].setKey(map.get(filter[i].getKey()));
            }
        SortAndFiltersInput sortAndFiltersInput = new SortAndFiltersInput(filter, sort);
        return sortAndFiltersInput;
    }

    public static SortAndFiltersInput rebuildQueryDsl(
            @SuppressWarnings("rawtypes") Pair<Map<String, String>, Map<String, Pair>> pair,
            SortAndFiltersInput query) {
        SortCriteria[] sort = query.getSort();
        SearchCriteria[] filter = query.getFilters();
        if (sort != null)
            for (int i = 0; i < sort.length; i++) {
                sort[i].setField(buildQueryDslPath(pair, sort[i].getField()));
            }

        if (filter != null)
            for (int i = 0; i < filter.length; i++) {
                filter[i].setKey(buildQueryDslPath(pair, filter[i].getKey()));
            }
        SortAndFiltersInput sortAndFiltersInput = new SortAndFiltersInput(filter, sort);
        return sortAndFiltersInput;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String buildQueryDslPath(Pair<Map<String, String>, Map<String, Pair>> pair, String field) {
        String elm[] = field.split("\\.");
        String res = "";
        Pair<Map<String, String>, Map<String, Pair>> pairTmp = pair;
        for (int i = 0; i < elm.length - 1; i++) {
            res += pairTmp.getValue0().get(elm[i]).length() != 0 ? (pairTmp.getValue0().get(elm[i]) + ".") : "";
            pairTmp = pairTmp.getValue1().get(elm[i]);
        }
        res += pairTmp.getValue0().get(elm[elm.length - 1]);
        return res;
    }
}
