import { useEffect, useState } from "react";
import { Box } from "@mui/material";
import { SearchContainer } from "./styled";
import {
  BasicDateTimePicker,
  CustomButton,
  Journey,
  LocationDropdown,
} from "../../components/index";
import { useJourneySearchQuery } from "../../hooks/useJourney";
import { useMutation } from "@tanstack/react-query";
import { Formik, useFormik } from "formik";
import * as Yup from "yup";
import AlertSpan from "../alert_span";

const validationSchema = Yup.object().shape({
  locationIdFrom: Yup.string().required("coming from..."),
  locationIdTo: Yup.string().required("heading to..."),
});

const initialValues = {
  locationIdFrom: "",
  locationIdTo: "",
  dateTimeFrom: "",
  dateTimeTo: "",
};

export default function Search() {
  const [selectedLeaving, setSelectedLeaving] = useState("");
  const [selectedGoing, setSelectedGoing] = useState("");
  const [journeys, setJourneys] = useState<any[]>();
  const [showResults, setShowResults] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [searchParams, setSearchParams] = useState(initialValues);

  // const formik = useFormik({
  //   initialValues: initialValues,
  //   validationSchema: validationSchema,
  //   onSubmit: (values) => {
  //     handleFormSubmit(values);
  //   },
  //   // enableReinitialize: true,
  // });

  const handleFormSubmit = (values: any) => {
    // console.log("useFormik values", values);
    setSearchParams((prevSearchParams) => ({
      ...prevSearchParams,
      locationIdFrom: selectedLeaving.value,
      // locationIdTo: selectedGoing.value,
      locationIdTo: values.locationIdTo,
    }));
  };

  const mutateSearchJourneys = useMutation({
    mutationFn: useJourneySearchQuery,
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
    if (searchParams.locationIdTo) {
      mutateSearchJourneys.mutate(searchParams);
    }
    // console.log("searchParams", searchParams);
  }, [searchParams]);

  const resultState = (state: boolean) => {
    setShowResults(state);
    setJourneys(undefined);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  const formik = useFormik({
    initialValues: initialValues,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    // enableReinitialize: true,
  });
  return (
    <Box component="form" noValidate onSubmit={formik.handleSubmit}>
      <SearchContainer>
        <LocationDropdown
          id="leaving-from-dropdown"
          label="Leaving From"
          selectedLocation={selectedLeaving}
          setSelectedLocation={setSelectedLeaving}
          // error={Boolean(formik.errors.locationIdFrom)}
          // helperText={
          //   formik.touched.locationIdFrom && formik.errors.locationIdFrom
          // }
        />
        <LocationDropdown
          id="going-to-dropdown"
          label="Going to"
          name="locationIdTo"
          selectedLocation={selectedGoing}
          setSelectedLocation={setSelectedGoing}
          value={formik.values.locationIdTo}
          onChange={formik.handleChange}
          // onChange={(value: unknown) =>
          //   formik.setFieldValue("locationIdTo", value, true)
          // }
          // setSelectedLocation={formik.setFieldValue("locationIdTo", "boo", true)}
          // setSelectedLocation={formik.handleChange}
          formikErrors={formik.errors.locationIdTo}
          formikTouched={formik.touched.locationIdTo}
        />
        <BasicDateTimePicker />
        <BasicDateTimePicker />
        <CustomButton type="submit" label="Search" />
      </SearchContainer>
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
          severity="warning"
          variant="filled"
          title="Oh no!"
          text="No rides for selected locations or dates. "
          state={handleCloseAlert}
        />
      )}
    </Box>
  );
}
