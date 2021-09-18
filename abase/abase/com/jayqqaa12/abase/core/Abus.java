package com.jayqqaa12.abase.core;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.squareup.otto.BasicBus;

@EBean(scope = Scope.Singleton)
public class Abus extends BasicBus {

}