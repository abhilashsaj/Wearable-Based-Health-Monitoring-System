import * as React from "react";
import { PostList, PostShow, PostCreate, PostEdit } from "./posts";
import { CommentList, CommentShow, CommentCreate, CommentEdit } from "./comments";
import { Admin, Resource, ListGuesser  } from "react-admin";
import {
  FirebaseDataProvider,
  FirebaseAuthProvider
} from "react-admin-firebase";
import CommentIcon from '@material-ui/icons/Comment';
import CustomLoginPage from './CustomLoginPage';

import { firebaseConfig as config } from './FIREBASE_CONFIG';
import { userHealthList, userHealthShow } from "./userHealth";
import { userHealthManualList, userHealthManualShow } from "./userHealthManual";
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';
import AssignmentIndIcon from '@material-ui/icons/AssignmentInd';
import DescriptionIcon from '@material-ui/icons/Description';
import PeopleAltIcon from '@material-ui/icons/PeopleAlt';

const options = {
  logging: true,
  watch: ['user_health'],
}
const dataProvider = FirebaseDataProvider(config, options);
const authProvider = FirebaseAuthProvider(config, options);

class App extends React.Component {
  render() {
    return (
      <Admin
        loginPage={CustomLoginPage} 
        dataProvider={dataProvider}
        authProvider={authProvider}
      >
      <Resource name="user_health"
      icon={PeopleOutlineIcon} 
      list={userHealthList} 
      show={userHealthShow}/>

      <Resource 
      name="users_manual" 
      icon= {DescriptionIcon}
      list={userHealthManualList} 
      show={userHealthManualShow}/>

      
      <Resource name="user_health_record" list={ListGuesser} icon={AssignmentIndIcon}/>
      
      
      <Resource name="users" list={ListGuesser} icon={PeopleAltIcon}/>
        
      </Admin>
    );
  }
}

export default App;