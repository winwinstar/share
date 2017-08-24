import React,{Component} from 'react';
import connectToStores from 'alt-utils/lib/connectToStores';
import IndexStore from 'stores/IndexStore.js';
import Layout from 'components/common/Layout';

class Test extends Component {
	render(){
		return(
			<Layout>
				<br/>
				Test2 Page
			</Layout>
		)
	}
}

export default Test;
