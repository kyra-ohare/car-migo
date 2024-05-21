import * as Yup from 'yup';

export const validationSchema = Yup.object().shape({
  locationIdFrom: Yup.string().required('coming from...'),
  locationIdTo: Yup.string().required('heading to...'),
  dateTimeFrom: Yup.string().required('date from...'),
  dateTimeTo: Yup.string().required('date to...'),
});
