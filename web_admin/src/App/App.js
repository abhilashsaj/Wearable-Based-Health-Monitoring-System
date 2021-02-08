import './App.css';
import SideMenu from '../components/SideMenu';
import { makeStyles, CssBaseline } from '@material-ui/core';
import Header from '../components/Header';
import PageHeader from '../components/PageHeader'
import PeopleOutlineIcon from '@material-ui/icons/PeopleOutline';
import EnhancedTable from '../components/EnhancedTable';
const useStyles = makeStyles({
  appMain: {
    paddingLeft: '320px',
    width: '100%'
  }
})

// <> means <React.Fragment>
function App() {
  const classes = useStyles();
  return (
    <>
      <SideMenu/>
      <div className={classes.appMain}>
        <Header/>
        <PageHeader
        title="Page Header"
        subtitle ="Page Description" 
        icon = {<PeopleOutlineIcon fontSize="large"/>}
        />
        <EnhancedTable/>
      </div>
      <CssBaseline/>
    </>
  );
}

export default App;
