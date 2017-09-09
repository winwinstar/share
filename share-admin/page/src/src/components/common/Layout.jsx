import React,{Component} from 'react';
import {Menu} from 'antd';
import {Link} from 'react-router';
import styles from 'components/common/Common.less';

const SubMenu = Menu.SubMenu;

class Layout extends React.Component {
	render(){
		return (
			<div>
				<Menu mode="horizontal">
					<SubMenu key="title1" title={<span>生活服务</span>}>
						<Menu.Item key="setting:1"><Link to="/">自动点餐</Link></Menu.Item>
					</SubMenu>
					<SubMenu key="title2" title={<span>兴趣爱好</span>}>
						<Menu.Item key="setting:2"><Link to="/test">工作生活</Link></Menu.Item>
					</SubMenu>
				</Menu>
				<div className={styles.content}>
					<br/>
                    {this.props.children}
				</div>
			</div>
		)
	}
}

export default Layout;
