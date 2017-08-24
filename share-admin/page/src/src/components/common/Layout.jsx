import React,{ Component } from 'react';
import { Menu, Icon, Spin, Tabs} from 'antd';
import {Link} from 'react-router';
import styles from 'components/common/Common.less';

const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
const TabPane = Tabs.TabPane;

class Layout extends React.Component {
	render(){
		return (
			<div>
				<Menu mode="horizontal">
					<SubMenu key="title1" title={<span>子菜列表项1</span>}>
						<Menu.Item key="setting:1"><Link to="/">子菜单项</Link></Menu.Item>
					</SubMenu>
					<SubMenu key="title2" title={<span>子菜列表项2</span>}>
						<Menu.Item key="setting:2"><Link to="/test">子菜单项2</Link></Menu.Item>
					</SubMenu>
				</Menu>
				<div className={styles.content}>
                    {this.props.children}
				</div>
			</div>
		)
	}
}

export default Layout;
