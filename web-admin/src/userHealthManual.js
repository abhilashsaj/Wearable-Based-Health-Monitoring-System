// in src/posts.js
import * as React from "react";
// tslint:disable-next-line:no-var-requires
import {
  Datagrid,
  List,
  Show,
  Create,
  Edit,
  Filter,
  SimpleShowLayout,
  SimpleForm,
  ReferenceField,
  ReferenceInput,
  TextField,
  TextInput,
  ShowButton,
  EditButton,
  DeleteButton,
  RichTextField,
  ChipField,
  SelectInput,
  FileField,
  DateField,
  FileInput
} from "react-admin";

const UserHealthFilterManual = (props) => (
  <Filter {...props}>
    <TextInput label="Search" source="username" alwaysOn />
  </Filter>
);

export const userHealthManualList = (props) => (
  <List {...props} filters={<UserHealthFilterManual />}>
    <Datagrid > 
        
        <TextField source="DEVICE_ID" />
        <TextField source="UID" />
        <TextField source="username" />
        <TextField source="currentTime" />
        <DateField source="currentDate" />
        <ChipField source="entry_type" />

        <TextField source="post_meal" />
        <TextField source="blood_sugar_level" />
        <TextField source="breaths_per_minute" />
        <TextField source="is_running" />
        <TextField source="breath_shortness_severity" />
        <TextField source="cough_frequency" />
        <TextField source="cough_severity" />
        <TextField source="blood_pressure_sys" />
        <TextField source="blood_pressure_dia" />
        <TextField source="heart_rate" />
        <TextField source="cholestorol" />
        <TextField source="oxygen_saturation" />
        <TextField source="diabetes" />
        <TextField source="chd" />
        <TextField source="asthma" />
        <TextField source="hypoxemia" />
        <TextField source="bronchi" />
        <TextField source="stress" />


    </Datagrid>
  </List>
);

export const userHealthManualShow = (props) => (
  <Show {...props}>
    <SimpleShowLayout>
      <TextField source="id" />
      <TextField source="username" />
    </SimpleShowLayout>
  </Show>
);

export const PostCreate = (props) => (
  <Create {...props} >
    <SimpleForm>
      <TextInput source="title" />
      <ReferenceInput label="Comment" source="title" reference="comments">
        <SelectInput optionText="title" />
      </ReferenceInput>
      <FileInput source="file" label="File" accept="application/pdf">
        <FileField source="src" title="title" />
      </FileInput>
    </SimpleForm>
  </Create>
);

export const PostEdit = (props) => (
  <Edit {...props}>
    <SimpleForm>
      <ReferenceInput source="id" options={{ disabled: true }} />
      <ReferenceInput source="createdate" options={{ disabled: true }} />
      <ReferenceInput source="lastupdate" options={{ disabled: true }} />
      <ReferenceInput label="Comment" source="title" reference="comments">
        <SelectInput optionText="title" />
      </ReferenceInput>
      <TextInput source="title" />
      <SelectInput source="rating" choices={[
        { id: 1, name: 'Good' },
        { id: 2, name: 'Okay' },
        { id: 3, name: 'Bad' },
      ]} />
      <FileInput source="file" label="File" accept="application/pdf">
        <FileField source="src" title="title" />
      </FileInput>
    </SimpleForm>
  </Edit>
);