package com.hust.baseweb.applications.tmscontainer.model;

import com.hust.baseweb.applications.tmscontainer.entity.ContTrailer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutputContTrailerModel {
    List<ContTrailer> lists;
}
