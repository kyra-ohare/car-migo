import * as Yup from 'yup';

export const createJourneyValidationSchema = Yup.object().shape({
  locationIdFrom: Yup.string().required('Origin must not be empty.'),
  locationIdTo: Yup.string().required('Destination must not be empty.'),
  dateTime: Yup.string().required('Date and time must not be empty.'),
  maxPassengers: Yup.number()
    .min(1)
    .max(10)
    .required('Max number of passengers must not be empty.'),
});
