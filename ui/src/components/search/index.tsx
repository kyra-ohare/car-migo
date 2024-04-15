/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Box } from '@mui/material';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import {
  AlertSpan,
  BasicDateTimePicker,
  CustomButton,
  Journey,
  LocationDropdown,
} from '../../components/index';
import { StyledSearchContainer } from './styled';
import { initialSearchValues } from './initial_values';
import { IJourneyEntity, ISearchFormValues } from '../../interfaces';
import { useJourneySearchQuery } from '../../hooks/useJourney';

const validationSchema = Yup.object().shape({
  locationIdFrom: Yup.string().required('coming from...'),
  locationIdTo: Yup.string().required('heading to...'),
  dateTimeFrom: Yup.string().required('date from...'),
  dateTimeTo: Yup.string().required('date to...'),
});

export default function Search() {
  console.log('SEARCH');
  const [selectedLeaving, setSelectedLeaving] = useState<string>('');
  const [selectedGoing, setSelectedGoing] = useState<string>('');
  const [journeys, setJourneys] = useState<IJourneyEntity[]>();
  const [showResults, setShowResults] = useState<boolean>(false);
  const [showAlert, setShowAlert] = useState<boolean>(false);
  const [searchParams, setSearchParams] = useState(initialSearchValues);

  const resultState = (state: boolean) => {
    setShowResults(state);
    setJourneys(undefined);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const handleFormSubmit = (values: ISearchFormValues) => {
    console.log('handleFormSubmit', values);
    setSearchParams((prevSearchParams) => ({
      ...prevSearchParams,
      locationIdFrom: values.locationIdFrom,
      dateTimeFrom: values.dateTimeFrom,
      dateTimeTo: values.dateTimeTo,
      locationIdTo: values.locationIdTo,
    }));
  };

  const formik = useFormik({
    initialValues: initialSearchValues,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
  });

  const mutateSearchJourneys = useMutation({
    mutationFn: useJourneySearchQuery,
    onSuccess: (data) => {
      console.log('SUCCESS', data);
      setShowAlert(false);
      setJourneys(data);
      setShowResults(true);
    },
    onError: (error) => {
      console.log('ERROR', error);
      setShowResults(false);
      setShowAlert(true);
    },
  });

  useEffect(() => {
    if (searchParams.locationIdTo) {
      mutateSearchJourneys.mutate(searchParams);
    }
  }, [searchParams]);

  return (
    <Box component='form' noValidate onSubmit={formik.handleSubmit}>
      <StyledSearchContainer>
        <LocationDropdown
          id='leaving-from-dropdown'
          label='Leaving From'
          name='locationIdFrom'
          selectedLocation={selectedLeaving}
          setSelectedLocation={setSelectedLeaving}
          onChange={formik.setFieldValue}
          value={formik.values.locationIdFrom}
          formikErrors={formik.errors.locationIdFrom}
          formikTouched={formik.touched.locationIdFrom}
          datatestid='leaving-from-dropdown'
        />
        <LocationDropdown
          id='going-to-dropdown'
          label='Going to'
          name='locationIdTo'
          selectedLocation={selectedGoing}
          setSelectedLocation={setSelectedGoing}
          value={formik.values.locationIdTo}
          onChange={formik.setFieldValue}
          formikErrors={formik.errors.locationIdTo}
          formikTouched={formik.touched.locationIdTo}
          datatestid='going-to-dropdown'
        />
        <BasicDateTimePicker
          label='Earliest Date/Time'
          name='dateTimeFrom'
          value={formik.values.dateTimeFrom}
          onChange={formik.setFieldValue}
          formikErrors={formik.errors.dateTimeFrom}
          formikTouched={formik.touched.dateTimeFrom}
          datatestid='earliest-date-time-picker'
        />
        <BasicDateTimePicker
          label='Latest Date/Time'
          name='dateTimeTo'
          value={formik.values.dateTimeTo}
          onChange={formik.setFieldValue}
          formikErrors={formik.errors.dateTimeTo}
          formikTouched={formik.touched.dateTimeTo}
          datatestid='latest-date-time-picker'
        />
        <CustomButton type='submit' label='Search' datatestid='search-submit-button' />
      </StyledSearchContainer>
      {showResults && journeys && journeys[0] && (
        <Journey
          results={journeys}
          departure={journeys[0].locationFrom.description}
          destination={journeys[0].locationTo.description}
          state={resultState}
        />
      )}
      {showAlert && (
        <AlertSpan
          severity='warning'
          variant='filled'
          title='Oh no!'
          text='No rides for selected locations or dates. '
          state={handleCloseAlert}
        />
      )}
    </Box>
  );
}
