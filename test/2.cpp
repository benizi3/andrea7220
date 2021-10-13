void Character::Enter(MenuScene* menu)
{
	menu->changeBarTitle("MenuScene/title_on_bar/role.png");
	menu->changeleftBottomButtonTitle("MenuScene/title_on_bottom_button/top.png");
	menu->changeRightBottomButtonTitle("MenuScene/title_on_bottom_button/return.png");
	menu->changeCenterBottomButtonTitle("MenuScene/title_on_bottom_button/ready_go.png");
	menu->changeCenterBottomButton2Title("MenuScene/title_on_bottom_button/setup.png");

	
	auto visibleSize = Director::getInstance()->getVisibleSize();
	auto context=Context::getInstance();
	context->openDB();
	Cursor countMission;
	DBUtil::getDataInfo("select * from mission where have_got_receive=1;",&countMission);
	if(countMission.size()>0){
		Node *node=menu->getChildByTag(TAG_MENU_SCENE_TOP_NODE)->getChildByTag(TAG_BUTTON_MENU_SCENE_MISSION);
		menu->addRedTips(Point(visibleSize.width*0.096,visibleSize.width*0.096),TAG_BUTTON_MENU_SCENE_MISSION_REDTIPS,node);	
	}

	Cursor countPromotion;
	DBUtil::getDataInfo("select * from sale where hot=1;",&countPromotion);
	if(countPromotion.size()>0){
		Node *node=menu->getChildByTag(TAG_MENU_SCENE_TOP_NODE)->getChildByTag(TAG_BUTTON_MENU_SCENE_PROMOTION);
		menu->addRedTips(Point(visibleSize.width*0.12,visibleSize.width*0.096),TAG_BUTTON_MENU_SCENE_PROMOTION_REDTIPS,node);	
	}

	Cursor countMessage;
	DBUtil::getDataInfo("select * from message where award_id is not null;",&countMessage);
	if(countMessage.size()>0){
		Node *node=menu->getChildByTag(TAG_MENU_SCENE_TOP_NODE)->getChildByTag(TAG_BUTTON_MENU_SCENE_MESSAGE);
		menu->addRedTips(Point(visibleSize.width*0.096,visibleSize.width*0.096),TAG_BUTTON_MENU_SCENE_MESSAGE_REDTIPS,node);	
	}

	//判断炼化的材料是否齐全
	Cursor equip;
	DBUtil::getDataInfo("select intensify_gold,id from equip;",&equip);
	int isMaterialEnough=0;
	auto user=Context::getInstance()->getUser();
	for(int i=0;i<equip.size();i++){
		int equip_id=atoi(equip[i]["id"].c_str());
		if(atoi(equip[i]["id"].c_str())!=0){
			int count=0;
			Cursor intensify_need;			
			DBUtil::getDataInfo("select intensify.item_id,intensify.count,user_item.item_count from intensify left join user_item on user_item.item_id=intensify.item_id where equip_id="+equip[i]["id"]+";",&intensify_need);			
			if(intensify_need.size()!=0){
				for(int j=0;j<intensify_need.size();j++){
					if(intensify_need[j]["item_count"].c_str()!=""&&atoi(intensify_need[j]["count"].c_str())<=atoi(intensify_need[j]["item_count"].c_str())){
						count++;
					}
				}
				if(count==intensify_need.size()&&atoi(equip[i]["intensify_gold"].c_str())<=user->getGold()&&atoi(equip[i]["user_level"].c_str())<=user->getLevel()){
					isMaterialEnough++;
				}	
			}	
		}

	}

	if(isMaterialEnough>0){
		auto bottomCenter2 = (Node*)menu->getChildByTag(TAG_NODE_MENU_SCENE_BOTTOM_CENTER2);
		auto button = dynamic_cast<Button*>(bottomCenter2->getChildByTag(TAG_BUTTON_MENU_SCENE_BOTTOM_CENTER2));
		menu->addRedTips(Point(button->getContentSize().width*0.96,visibleSize.width*0.096),TAG_BUTTON_MENU_SCENE_MESSAGE_REDTIPS,button);
	}

	context->closeDB();

};

// 离开当前状态时，要执行的代码
void Character::Exit(MenuScene* menu)
{
	
};

// 每帧update时，要执行的代码
void Character::onUpdate(MenuScene* menu, float dt)
{

};

// 点击左下角按钮排行，要执行的代码
void Character::onClickLeftBottomButton(MenuScene* menu)
{
	bool result=Context::getInstance()->getIsOfflineMode();
	if(result){
		auto context = Context::getInstance();
		auto dialogLayer=MiddleSizeDialogLayer::create();		  
		dialogLayer->popUp(menu);						  
		dialogLayer->addContent(context->getStringByName("offline_close"));		  
		dialogLayer->addButtomButton("MiddleSizeDialogLayer/cancel.png","MiddleSizeDialogLayer/reLogin.png"); 
		auto buttonLeft =(Button *)dialogLayer->getChildByTag(TAG_IMAGE_MIDDLESIZEDIALOGLAYER_BGDIAOLG)->getChildByTag(TAG_BUTTON_MIDDLESIZEDIALOGLAYER_LEFT);	 
		buttonLeft->setTag(TAG_BUTTON_BASE_MENU_LAYER_CLOSE);  
		buttonLeft->addTouchEventListener(dialogLayer, toucheventselector(MiddleSizeDialogLayer::touchEvent)); 
		auto buttonRight =(Button *)dialogLayer->getChildByTag(TAG_IMAGE_MIDDLESIZEDIALOGLAYER_BGDIAOLG)->getChildByTag(TAG_BUTTON_MIDDLESIZEDIALOGLAYER_RIGHT); 
		buttonRight->addTouchEventListener(menu, toucheventselector(MenuScene::touchEvent)); 
		menu->setButtonTouchEnabled(true);
	}else{
		menu->getRank();
	}
};

// 点击右下角按钮（返回），要执行的代码
void Character::onClickRightBottomButton(MenuScene* menu)
{
	auto origin = Director::getInstance()->getVisibleOrigin();
	auto visibleSize = Director::getInstance()->getVisibleSize();
	auto hero = menu->getChildByTag(TAG_ARMATURE_MENU_SCENE_HERO);
	hero->runAction(Sequence::create(CallFuncN::create(CC_CALLBACK_0(MenuScene::beforeHeroUp,menu)),
			        DelayTime::create(ANIMATE_DURATION_TIME+ANIMATE_ROTATION_TIME),
			        MoveTo::create(ANIMATE_DURATION_TIME,(Point(visibleSize.width/2 + origin.x, visibleSize.height + origin.y + hero->getContentSize().height))),
					CallFuncN::create(CC_CALLBACK_0(MenuScene::changeState,menu,new Default())),
					CallFuncN::create(CC_CALLBACK_0(MenuScene::afterHeroUp,menu)),
					DelayTime::create(ANIMATE_DURATION_TIME+ANIMATE_DURATION_TIME),
			        CallFuncN::create(CC_CALLBACK_0(MenuScene::setButtonTouchEnabled,menu,true)),
					NULL));
};

// 点击中间主按钮（开始游戏、出击），要执行的代码
void Character::onClickCenterBottomButton(MenuScene* menu)
{			
	menu->changeState(new Go());
};

// 点击中间副按钮（整备），要执行的代码
void Character::onClickCenterBottomButton2(MenuScene* menu)
{
	auto origin = Director::getInstance()->getVisibleOrigin();
	auto visibleSize = Director::getInstance()->getVisibleSize();
	auto bottom_img_left = menu->getChildByTag(TAG_IMG_MENU_SCENE_BOTTOM_BACKGROUND_LEFT);
	auto bottom_img_scale = visibleSize.width*0.5f/bottom_img_left->getContentSize().width;
	auto subMenuLayer = SetupMenuLayer::create();
	subMenuLayer->popUp(menu);
	subMenuLayer->setVisible(false);
	subMenuLayer->setButtonVisible(false);
	subMenuLayer->setButtonTouchEnable(false);
	menu->setLowerNode(false);
	subMenuLayer->setButtonVisible(true);
	menu->setBottomPosition(true,true,true,true,true,true,true);
	menu->changeState(new Setup());
	menu->runAction(Sequence::create(CallFuncN::create(CC_CALLBACK_0(MenuScene::startInteractiveAnimation,menu,true,true,true,Point(origin.x+visibleSize.width/2,origin.y+visibleSize.width*(-0.0953125f)),false,true)),
					DelayTime::create(ANIMATE_DURATION_TIME),		            
					CallFuncN::create(CC_CALLBACK_0(MenuScene::moveLowerNode,menu,ANIMATE_ROTATION_TIME,Point(origin.x,bottom_img_scale*bottom_img_left->getContentSize().height))),
					DelayTime::create(ANIMATE_ROTATION_TIME),
					CallFuncN::create(CC_CALLBACK_0(MenuScene::setLayerVisible,menu,subMenuLayer,true)),
			        CallFuncN::create(CC_CALLBACK_0(MenuScene::setButtonTouchEnabled,menu,true)),
					CallFuncN::create(CC_CALLBACK_0(SetupMenuLayer::setButtonTouchEnable,subMenuLayer,true)),
					NULL));		
};

// 用户按了手机的返回键
void Character::onPressBackKey()
{

};