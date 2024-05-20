/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Box } from '@mui/material';
import { useFormik } from 'formik';
import {
  AlertSpan,
  BasicDateTimePicker,
  Dropdown,
  CustomButton,
  Journey,
} from '../../components/index';
import { StyledSearchContainer } from './styled';
import { initialSearchValues } from './initial_values';
import { IJourneyEntity, ISearchFormValues } from '../../interfaces';
import { useJourneySearch } from '../../hooks/useJourney';
import { locations } from '../../constants';
import { validationSchema } from './validation_schema';

export default function Search() {
  const [selectedLeaving, setSelectedLeaving] = useState<string>('');
  const [selectedGoing, setSelectedGoing] = useState<string>('');
  const [journeys, setJourneys] = useState<IJourneyEntity[]>();
  const [showResults, setShowResults] = useState<boolean>(false);
  const [showAlert, setShowAlert] = useState<boolean>(false);
  const [searchParams, setSearchParams] = useState(initialSearchValues);

  const handleFormSubmit = (values: ISearchFormValues) => {
    setSearchParams((prevSearchParams) => ({
      ...prevSearchParams,
      locationIdFrom: values.locationIdFrom,
      locationIdTo: values.locationIdTo,
      dateTimeFrom: values.dateTimeFrom,
      dateTimeTo: values.dateTimeTo,
    }));
  };

  const formik = useFormik({
    initialValues: initialSearchValues,
    validationSchema: validationSchema,
    onSubmit: (values: ISearchFormValues) => {
      handleFormSubmit(values);
    },
  });

  const mutateSearchJourneys = useMutation({
    mutationFn: useJourneySearch,
    onSuccess: (data) => {
      setShowAlert(false);
      setJourneys(data);
      setShowResults(true);
    },
    onError: () => {
      setShowResults(false);
      setShowAlert(true);
    },
  });

  useEffect(() => {
    setJourneys(undefined);
    if (
      searchParams.locationIdTo ||
      searchParams.locationIdFrom ||
      searchParams.dateTimeFrom ||
      searchParams.dateTimeTo
    ) {
      mutateSearchJourneys.mutate(searchParams);
    }
  }, [searchParams]);

  return (
    <Box component='form' noValidate onSubmit={formik.handleSubmit}>
      <StyledSearchContainer>
        <Dropdown
          id='leaving-from-dropdown'
          label='Leaving From'
          name='locationIdFrom'
          options={locations}
          selectedOption={selectedLeaving}
          setSelectedOption={setSelectedLeaving}
          onChange={formik.setFieldValue}
          value={formik.values.locationIdFrom}
          formikErrors={formik.errors.locationIdFrom}
          formikTouched={formik.touched.locationIdFrom}
          widthStyle={300}
          mrStyle={0.5}
          datatestid='leaving-from-dropdown'
        />
        <Dropdown
          id='going-to-dropdown'
          label='Going to'
          name='locationIdTo'
          options={locations}
          selectedOption={selectedGoing}
          setSelectedOption={setSelectedGoing}
          value={formik.values.locationIdTo}
          onChange={formik.setFieldValue}
          formikErrors={formik.errors.locationIdTo}
          formikTouched={formik.touched.locationIdTo}
          widthStyle={300}
          mrStyle={0.5}
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
        <CustomButton
          type='submit'
          label='Search'
          datatestid='search-submit-button'
        />
      </StyledSearchContainer>
      {showResults && journeys && journeys[0] && (
        <Journey
          label='search'
          journeys={journeys}
          origin={journeys[0].locationFrom.description}
          destination={journeys[0].locationTo.description}
          datatestid='journey-component'
        />
      )}
      {showAlert && (
        <AlertSpan
          severity='warning'
          variant='filled'
          title='Oh no!'
          text='No rides for selected locations or dates. '
          state={() => {
            setShowAlert(false);
          }}
        />
      )}
    </Box>
  );
}
