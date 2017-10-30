/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package org.apache.griffin.core.measure;

import org.apache.griffin.core.measure.repo.MeasureRepo;
import org.apache.griffin.core.util.URLHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MeasureOrgController.class, secure = false)
public class MeasureOrgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasureRepo measureRepo;


    @Test
    public void testGetOrgs() throws Exception {
        String org = "orgName";
        when(measureRepo.findOrganizations()).thenReturn(Arrays.asList(org));

        mockMvc.perform(get(URLHelper.API_VERSION_PATH + "/org"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", is(org)));
    }

    @Test
    public void testGetMetricNameListByOrg() throws Exception {
        String org = "hadoop";
        when(measureRepo.findNameByOrganization(org)).thenReturn(Arrays.asList(org));

        mockMvc.perform(get(URLHelper.API_VERSION_PATH + "/org/{org}", org))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", is(org)));
    }

    @Test
    public void testGetMeasureNamesGroupByOrg() throws Exception {
        List<String> orgs = Arrays.asList("orgName");
        when(measureRepo.findOrganizations()).thenReturn(orgs);
        when(measureRepo.findNameByOrganization(orgs.get(0))).thenReturn(Arrays.asList("measureName"));

        mockMvc.perform(get(URLHelper.API_VERSION_PATH + "/org/measure/names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orgName", hasSize(1)));
    }

}