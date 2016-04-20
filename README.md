Clever Section Recycler View
=============================

Hey! It's a custom recycler view library, witch can display your data in sections (with Header and Footer). Okey, you can say that it's an average recycler view, but what makes it clever, see it below.:

- User can use different view type in each section, so you can use a linear layout type witch span the whole screen in first section and display a grid layout type in the second section.

- Supports multiple view type in every view type (*header,item,footer*).

- Supports endless scrolling.

- You can use drag&drop function within sections.


![alt tag](https://github.com/kgeriiie/cleversectionview/blob/master/images/clever_section_recyclerview_320.gif)

----------

Useage
----------

CSRV provides base classes for sections and section items. You should inherite from this classes.

>**Note:** use unique **id** in *getId()* methods, because it will be used for identify row items in recycler view adapters. Not unique ids can cause bad working of CSRV.

***Inherite from BaseSectionModel***
```java
public class Section extends BaseSectionModel {

    ...

	@Override
    public String getId() {
	    //It has to be unique.
        return id;
    }

    @Override
    public List<SectionItem> getSectionItems() {
        return proposerItems;
    }

	...

}
```

***Inherite from BaseSectionItemModel***
```java
public class SectionItem extends
BaseSectionItemModel {

	...

    @Override
    public String getId() {
	    //It has to be unique.
        return id;
    }

	...

}
```
***Inherite from BaseCleverSectionAdapter***
```java
public class SectionAdapter extends
BaseCleverSectionAdapter<Section,SectionItem,BaseDragAndDropViewHolder,ProposerHeaderViewHolder,ProposerFooterViewHolder> {

	...

    @Override
    public ProposerHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int layoutType) {
	    ...
    }

	@Override
    public BaseDragAndDropViewHolder onCreateItemViewHolder(ViewGroup parent, int layoutType) {
		...
	}

    @Override
    public ProposerFooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int layoutType) {
		...
	}

    @Override
    public void onBindHeaderViewHolder(ProposerHeaderViewHolder holder,Proposer section, int layoutType) {
		...
	}

    @Override
    public void onBindItemViewHolder(BaseDragAndDropViewHolder holder,ProposerItem item, int layoutType) {
		...
	}

	@Override
    public void onBindFooterViewHolder(ProposerFooterViewHolder holder,Proposer section, int layoutType) {
		...
	}

	...

}
```

>**Note:** You can see that it is same to the ordinary RecylerViewAdapter. The difference is that we have to implement **onCreate...ViewHolder** and **onBind...ViewHolder** every row (header, footer, item). Both of method you can find a new parameter called viewType. It's equals with viewType (HEADER,ITEM or FOOTER) in default case, but you can customize them, if you override **getLayoutType()** method in children of BaseSectionItemModel class.

####Setup span of each row.
As I mentioned you can use different layout type in each sections. In default case we use *LINEAR* (row with match with parent) layout type. But if we override the **getSpanType()** method in children of BaseSectionItemModel class, we can customize the appearance of row item.

```java
    @Override
    public int getSpanType() {
        if (getLayoutType() == LAYOUT_TYPE.CARD_LAYOUT) {
            return SPAN_TYPE.LINEAR_TYPE;
        } else {
            return SPAN_TYPE.GRID_TYPE;
        }
    }
```

>**Note:** Try to use same span type at every section item in the same section.

####Limit drag & drop working on each item.
We can limit the drag and drop function on items, if we need. In this case we can't drag item from this position and we can't drop item to that limited position. To use this limitation, we should override isDraggingEnabledAtItemPosition() method in children of BaseCleverSectionAdapter's class.

*Etc.: disable first GRID span typed item drag or drop to his position.*
```java
    @Override
    public boolean isDraggingEnabledAtItemPosition(Proposer section,ProposerItem fromItem, ProposerItem toItem) {

        if ((fromItem.getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE && section.getItemIndexInSection(fromItem) == 0) ||
                (toItem.getSpanType() == BaseSectionItemModel.SPAN_TYPE.GRID_TYPE && section.getItemIndexInSection(toItem) == 0)) {
            return false;
        }

        return super.isDraggingEnabledAtItemPosition(section,fromItem, toItem);
    }
```

If we have never need this function, we can disable it, just call **setDragAndDropEnabled()** method on adapter.

License
-------

    Copyright 2016 geriiie

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.