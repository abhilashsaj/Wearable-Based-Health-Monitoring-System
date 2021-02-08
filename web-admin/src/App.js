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
      list={userHealthList} 
      show={userHealthShow}/>
      <Resource name="user_health_record" list={ListGuesser} icon={CommentIcon}/>
      
      <Resource 
      name="users_manual" 
      list={userHealthManualList} 
      show={userHealthManualShow}/>
      <Resource name="users" list={ListGuesser} />
        <Resource
          name="posts"
          list={PostList}
          show={PostShow}
          create={PostCreate}
          edit={PostEdit}
        />
        <Resource
          name="comments"
          icon={CommentIcon}
          list={CommentList}
          show={CommentShow}
          create={CommentCreate}
          edit={CommentEdit}
        />
      </Admin>
    );
  }
}

export default App;