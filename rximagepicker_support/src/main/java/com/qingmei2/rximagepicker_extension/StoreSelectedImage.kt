package com.qingmei2.rximagepicker_extension

import com.qingmei2.rximagepicker_extension.entity.Item
import java.util.ArrayList

class StoreSelectedImage{

    companion object{
        open var storeItems : ArrayList<Item> = ArrayList()
    }
}